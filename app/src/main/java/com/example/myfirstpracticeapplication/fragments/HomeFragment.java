package com.example.myfirstpracticeapplication.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myfirstpracticeapplication.DetailLinkActivity;
import com.example.myfirstpracticeapplication.R;
import com.example.myfirstpracticeapplication.adapters.HomeInfoListAdapter;
import com.example.myfirstpracticeapplication.model.HomeDataResult;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private ImageView mAdbannerIv,mRecommendedCompanyIv;
    private View mRootView;
    private RecyclerView mRecyclerView;
    private Handler mHandler = new Handler();
    private Context mContext;

    private HomeDataResult homeDataResult;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView =  inflater.inflate(R.layout.fragment_home, container, false);
        this.mContext = getActivity();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdbannerIv = mRootView.findViewById(R.id.adbanner_iv);
        mRecommendedCompanyIv = mRootView.findViewById(R.id.recommended_company);
        mRecyclerView = mRootView.findViewById(R.id.rv_industry_information);

        mAdbannerIv.setOnClickListener(this);
        mRecommendedCompanyIv.setOnClickListener(this);

        requestHomeData();
    }

    private void requestHomeData() {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构建参数表单
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("appid","1");
        //3.构建请求
        Request request = new Request.Builder().url("http://v2.ffu365.com/index.php?m=Api&c=Index&a=home")
                .post(builder.build()).build();
        //4.开始发送请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("TAG", "onResponse: result" + result);

                Gson gson = new Gson();
                homeDataResult = gson.fromJson(result,HomeDataResult.class);

                showHomeData();
            }
        });
    }

    private void showHomeData() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //显示第一张图片
                String adBannerImage = homeDataResult.getData().getAd_list().get(0).getImage();
                Glide.with(mContext).load(adBannerImage).into(mAdbannerIv);
                //显示第二张图片
                String recommendedCompanyImage = homeDataResult.getData().getCompany_list().get(0).getImage();
                Glide.with(mContext).load(recommendedCompanyImage).into(mRecommendedCompanyIv);
                //显示recycler列表
                mRecyclerView.setAdapter(new HomeInfoListAdapter(homeDataResult.getData().getNews_list()));
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adbanner_iv:
                Intent intent = new Intent();
                intent.putExtra(DetailLinkActivity.URL_KEY,homeDataResult.getData().getAd_list().get(0).getLink());
                startActivity(intent);
                break;
            case R.id.recommended_company:
                Intent intent2 = new Intent();
                intent2.putExtra(DetailLinkActivity.URL_KEY,homeDataResult.getData().getCompany_list().get(0).getLink());
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
    //我现在是在分支test01上修改；我现在是在分支test01上修改；我现在是在分支test01上修改

    //我现在是在分支test02上修改；我现在是在分支test02上修改；
}
