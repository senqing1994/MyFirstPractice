package com.example.myfirstpracticeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.myfirstpracticeapplication.util.ActivityManageUtil;

public class WelcomeActivity extends AppCompatActivity {

    //welcome page when launcher app
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ActivityManageUtil.getInstance().addActivity(this);

        initView();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManageUtil.getInstance().finishActivity(this);
    }

    private void initData() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"alpha",0.5f,1f);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        imageView = findViewById(R.id.app_welcome);
    }
}
