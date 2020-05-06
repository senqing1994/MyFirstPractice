package com.example.myfirstpracticeapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.myfirstpracticeapplication.adapters.HomePageAdapter;
import com.example.myfirstpracticeapplication.fragments.CenterFragment;
import com.example.myfirstpracticeapplication.fragments.CollectionFragment;
import com.example.myfirstpracticeapplication.fragments.HomeFragment;
import com.example.myfirstpracticeapplication.fragments.MessageFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private RadioButton rb_home,rb_collection,rb_message,rb_center;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new CollectionFragment());
        fragments.add(new MessageFragment());
        fragments.add(new CenterFragment());

        HomePageAdapter homePageAdapter = new HomePageAdapter(getSupportFragmentManager(),fragments);

        viewPager.setAdapter(homePageAdapter);
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);

        rb_home = findViewById(R.id.rb_home);
        rb_collection = findViewById(R.id.rb_collection);
        rb_message = findViewById(R.id.rb_message);
        rb_center = findViewById(R.id.rb_center);

        rb_home.setOnClickListener(this);
        rb_collection.setOnClickListener(this);
        rb_message.setOnClickListener(this);
        rb_center.setOnClickListener(this);

        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_home:
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.rb_collection:
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.rb_message:
                viewPager.setCurrentItem(2,false);
                break;
            case R.id.rb_center:
                viewPager.setCurrentItem(3,false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                rb_home.setChecked(true);
                break;
            case 1:
                rb_collection.setChecked(true);
                break;
            case 2:
                rb_message.setChecked(true);
                break;
            case 3:
                rb_center.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
