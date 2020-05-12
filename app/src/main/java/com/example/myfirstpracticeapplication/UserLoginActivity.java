package com.example.myfirstpracticeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstpracticeapplication.model.UserLoginResult;
import com.example.myfirstpracticeapplication.util.ActivityManageUtil;
import com.example.myfirstpracticeapplication.util.CommonUtil;
import com.example.myfirstpracticeapplication.util.MD5Util;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "UserLoginActivity";
    private EditText mEtUserPhone,mEtUserPassword;
    private Button mBtUserLogin;
    private TextView mTvUserRegister;
    private CheckBox mCbCheckPassword;

    //Handler use for switch to main thread
    private static Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ActivityManageUtil.getInstance().addActivity(this);

        mEtUserPhone = findViewById(R.id.user_phone_et);
        mEtUserPassword = findViewById(R.id.user_password_et);
        mBtUserLogin = findViewById(R.id.user_login_bt);
        mTvUserRegister = findViewById(R.id.user_register_tv);
        mCbCheckPassword = findViewById(R.id.check_password_cb);

        mBtUserLogin.setOnClickListener(this);
        mTvUserRegister.setOnClickListener(this);
        mCbCheckPassword.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_login_bt:
                String userPhone = mEtUserPhone.getText().toString().trim();
                String userPassword = mEtUserPassword.getText().toString().trim();
                if(TextUtils.isEmpty(userPhone)){
                    Toast.makeText(this,"请输入手机号",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(userPassword)){
                    Toast.makeText(this,"请输入密码",Toast.LENGTH_LONG).show();
                    return;
                }
                //请求服务器
                requestServerLogin(userPhone,userPassword);
                break;
            case R.id.user_register_tv:
                Intent intent = new Intent(this,UserRegisterActivity.class);
                startActivityForResult(intent,0);
                break;
            default:
                break;
        }
    }

    private void requestServerLogin(String userPhone, String userPassword) {
        //1.构建okhttpclient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构建请求体的body
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("appid","1");
        builder.addFormDataPart("cell_phone",userPhone);
        builder.addFormDataPart("password", MD5Util.strToMd5(userPassword));

        final String URL = "http://v2.ffu365.com/index.php?m=Api&c=Member&a=login";
        //3.构建一个请求
        Request request =new Request.Builder().url(URL)
                .post(builder.build())
                .build();

        //4.发送请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "onResponse: "+ result);
                Gson gson = new Gson();
                final UserLoginResult userLoginResult = gson.fromJson(result,UserLoginResult.class);
                Log.d(TAG, "onResponse: user info result: " + result);

                //保存用户信息用于刷新界面
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dealLoginResult(userLoginResult);
                    }
                });
            }
        });
    }

    /**
     * 请求之后处理返回的数据
     * @param userLoginResult
     */
    private void dealLoginResult(UserLoginResult userLoginResult) {
        //判断是否成功
        if(userLoginResult.getErrcode() == 1){
            //1.保存登陆状态，当前设置为已登录
            SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
            sp.edit().putBoolean("is_login",true).apply();
            //2.需要保存用户信息
            UserLoginResult.DataBean dataBean = userLoginResult.getData();
            Gson gson = new Gson();
            String userInfoString = gson.toJson(dataBean);
            sp.edit().putString("user_info",userInfoString).apply();
            //3.关掉这个页面
            finish();
        }else {
            Toast.makeText(this,userLoginResult.getErrmsg(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            mEtUserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            mEtUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        //将密码框光标移动至最后
        CommonUtil.cursorToEnd(mEtUserPassword);
    }
}
