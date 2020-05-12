package com.example.myfirstpracticeapplication.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myfirstpracticeapplication.R;
import com.example.myfirstpracticeapplication.UserInfoActivity;
import com.example.myfirstpracticeapplication.UserLoginActivity;
import com.example.myfirstpracticeapplication.model.UserLoginResult;
import com.example.myfirstpracticeapplication.ui.RoundImageView;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    //未登录状态的登录界面
    private TextView mTextView;
    //已登录的个人信息入口
    private LinearLayout mLlUserInfo;
    //根界面
    private View mRootView;
    //已登录状态下头像、姓名和地址信息
    private ImageView mRivUserHead;
    private TextView mTvUserName,mTvUserAddress;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public CenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_center, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTextView = mRootView.findViewById(R.id.user_login_tv);
        mLlUserInfo = mRootView.findViewById(R.id.user_logined_ll);
        mRivUserHead = mRootView.findViewById(R.id.user_head_iv);
        mTvUserName = mRootView.findViewById(R.id.user_name_tv);
        mTvUserAddress = mRootView.findViewById(R.id.user_location_tv);

        mTextView.setOnClickListener(this);
        mLlUserInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_login_tv:
                Intent intent = new Intent(mContext, UserLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.user_logined_ll:
                Intent intent1 = new Intent(mContext, UserInfoActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = mContext.getSharedPreferences("info",Context.MODE_PRIVATE);
        boolean is_login = sp.getBoolean("is_login",false);
        String userInfoString = sp.getString("user_info",null);
        if(is_login){
            mTextView.setVisibility(View.GONE);
            mLlUserInfo.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(userInfoString)){
                //通过Gson将获取到的用户信息String转换为对象
                Gson gson = new Gson();
                UserLoginResult.DataBean userBean = gson.fromJson(userInfoString,UserLoginResult.DataBean.class);
                //加载头像和姓名以及位置
                Glide.with(mContext).load(userBean.getMember_info().getMember_avatar()).into(mRivUserHead);
                mTvUserName.setText(userBean.getMember_info().getMember_name());
                mTvUserAddress.setText(userBean.getMember_info().getMember_location_text());
            }
        }else {
            mTextView.setVisibility(View.VISIBLE);
            mLlUserInfo.setVisibility(View.GONE);
        }
    }
}
