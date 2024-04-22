package com.zgcwkj.jdcookie;

import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    String url = "https://plogin.m.jd.com/login/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView mWebview = findViewById(R.id.my_webview);
        mWebview.setWebViewClient(new WebViewClient());//不跳转浏览器
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebview.getSettings().setSupportZoom(false);//是否可以缩放，默认true
        mWebview.getSettings().setBuiltInZoomControls(false);//是否显示缩放按钮，默认false
        mWebview.getSettings().setUseWideViewPort(false);//设置此属性，可任意比例缩放。大视图模式
        mWebview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebview.getSettings().setDatabaseEnabled(false);//是否使用缓存
        mWebview.getSettings().setDomStorageEnabled(true);//DOM Storage
        mWebview.loadUrl(url);//打开网站

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CookieManager cookieManager = CookieManager.getInstance();
                String cookieStr = cookieManager.getCookie(url);

                String[] cookies = cookieStr.split(";");
                String ptKey = "";
                String ptPin = "";
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].contains("pt_key")) ptKey = cookies[i];
                    if (cookies[i].contains("pt_pin")) ptPin = cookies[i];
                }
                EditText editText = findViewById(R.id.editTextView);
                editText.setText(ptKey + ";" + ptPin + ";");
            }
        });
    }
}