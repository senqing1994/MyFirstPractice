package com.example.myfirstpracticeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class DetailLinkActivity extends AppCompatActivity {

    public static final String URL_KEY = "URL_KEY";

    private WebView webView;
    private WebSettings webSettings;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_link_avtivity);

        webView = findViewById(R.id.web_view);

        webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(false);  // 将图片调整到适合webview的大小
        webSettings.setJavaScriptEnabled(true); // 支持js
        webSettings.setLoadsImagesAutomatically(true);  // 支持自动加载图片

        mUrl = getIntent().getStringExtra(URL_KEY);
        webView.loadUrl(mUrl);
    }
}
