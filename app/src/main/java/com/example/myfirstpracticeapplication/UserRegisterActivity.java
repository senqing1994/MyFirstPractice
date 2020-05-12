package com.example.myfirstpracticeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstpracticeapplication.model.UserLoginResult;
import com.example.myfirstpracticeapplication.model.UserRequestCodeResult;
import com.example.myfirstpracticeapplication.ui.VerificationCodeButton;
import com.example.myfirstpracticeapplication.util.ActivityManageUtil;
import com.example.myfirstpracticeapplication.util.CommonUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用户注册活动
 */
public class UserRegisterActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static final String TAG = "UserRegisterActivity";
    //phone  password  verification_code
    private EditText mEtUserPhone,mEtUserPassword,mEtUserCode;
    //register button
    private Button mBtUserRegister;
    //show or conceal password
    private CheckBox mCbCheckPassword;
    //send verification code
    private VerificationCodeButton verificationCodeButton;
    //set agreement
    private TextView mTvUserAgreement;

    //Handler use for switch to main thread
    private static Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        ActivityManageUtil.getInstance().addActivity(this);
        initView();
    }

    /**
     * initialize view and register event
     */
    private void initView() {
        mEtUserPhone = findViewById(R.id.user_phone_et);
        mEtUserPassword = findViewById(R.id.user_password_et);
        mEtUserCode = findViewById(R.id.user_code_et);
        mBtUserRegister = findViewById(R.id.user_register_bt);
        mTvUserAgreement = findViewById(R.id.user_agreement_tv);
        mCbCheckPassword = findViewById(R.id.check_password_cb);
        verificationCodeButton = findViewById(R.id.send_code_bt);

        mTvUserAgreement.setText(Html.fromHtml("我已阅读并同意<font color='#24cfa2'>《享学咨询》</font>"));
        mCbCheckPassword.setOnCheckedChangeListener(this);

        mBtUserRegister.setOnClickListener(this);
        verificationCodeButton.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            mEtUserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            mEtUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        CommonUtil.cursorToEnd(mEtUserPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_register_bt:
                dealUserRegister();
                break;
            case R.id.send_code_bt:
                verificationCodeButton.startLoad();
                requestUserCode();
                break;
            default:
                break;
        }
    }

    private void dealUserRegister() {
        String userPhone = mEtUserPhone.getText().toString().trim();
        String userPassword = mEtUserPassword.getText().toString().trim();
        String verificationCode = mEtUserCode.getText().toString().trim();
        if(TextUtils.isEmpty(userPhone)){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(verificationCode)){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_LONG).show();
            return;
        }
        //发送网络请求
        //1.创建OkHttpclient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构建请求体
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("appid","1");
        builder.addFormDataPart("verify_code",verificationCode);
        builder.addFormDataPart("cell_phone",userPhone);
        builder.addFormDataPart("password",userPassword);

        final String URL = "http://v2.ffu365.com/index.php?m=Api&c=Member&a=register";
        //3.构建请求
        Request request = new Request.Builder().url(URL)
                .post(builder.build())
                .build();
        //4.发送请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //注册失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                final UserLoginResult userLoginResult = gson.fromJson(result,UserLoginResult.class);
                Log.d(TAG, "onResponse: result "+ result);
                //保存用户信息用于更新UI
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dealRegisterResult(userLoginResult);
                    }
                });
            }
        });
    }

    private void dealRegisterResult(UserLoginResult userLoginResult) {
        //判断是否注册成功
        if(userLoginResult.getErrcode() == 1){
            SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
            //1.保存登录状态
            sp.edit().putBoolean("is_login",true).apply();
            //2.保存返回的用户信息
            UserLoginResult.DataBean dataBean = userLoginResult.getData();
            Gson gson = new Gson();
            String userInfoString = gson.toJson(dataBean);
            sp.edit().putString("user_info",userInfoString).apply();
            //3.关闭当前注册页面，并关闭登陆页面
            ActivityManageUtil.getInstance().finishActivity(this);
            ActivityManageUtil.getInstance().finishActivity(UserLoginActivity.class);
        }else {
            Toast.makeText(this,userLoginResult.getErrmsg(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 请求服务器，获取验证码
     */
    private void requestUserCode() {
        String userPhone = mEtUserPhone.getText().toString().trim();
        if(TextUtils.isEmpty(userPhone)){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_LONG).show();
            return;
        }

        //向服务器发送请求
        OkHttpClient okHttpClient = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("appid","1");
        builder.addFormDataPart("sms_type","3");
        builder.addFormDataPart("cell_phone",userPhone);

        final String URL = "http://v2.ffu365.com/index.php?m=Api&c=Util&a=sendVerifyCode";
        Request request = new Request.Builder().url(URL)
                .post(builder.build()).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                final UserRequestCodeResult userRequestCodeResult = gson.fromJson(result,UserRequestCodeResult.class);
                Log.d(TAG, "onResponse: verification code : " + result);
                //发送验证码后更新UI
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dealCodeResult(userRequestCodeResult);
                    }
                });
            }
        });
    }

    private void dealCodeResult(UserRequestCodeResult userRequestCodeResult) {
        if(userRequestCodeResult.errcode == 1){
            verificationCodeButton.aginAfterTime(60);
        }else {
            Toast.makeText(this,"验证码是："+userRequestCodeResult.errmsg,Toast.LENGTH_LONG).show();
            verificationCodeButton.setNoraml();
        }
    }
}
