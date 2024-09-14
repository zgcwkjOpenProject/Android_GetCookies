package com.zgcwkj.getcks.browser;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.zgcwkj.bllcode.CookieHep;
import com.zgcwkj.bllcode.DialogLoading;
import com.zgcwkj.bllcode.SqliteHelp;
import com.zgcwkj.getcks.R;
import com.zgcwkj.getcks.StaticObj;
import com.zgcwkj.models.WebData;

public class BrowserFragment extends Fragment {
    public static BrowserHandler handler;//消息传递
    public BrowserViewModel viewModel;//页面模型

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var context = this.getContext();
        //传递消息
        handler = new BrowserHandler(Looper.myLooper(), this);
        //视图模型
        viewModel = new ViewModelProvider(this).get(BrowserViewModel.class);
        //视图
        var view = inflater.inflate(R.layout.fragment_browser, container, false);
        //浏览器
        var mWebview = (WebView) view.findViewById(R.id.my_webview);
        mWebview.setWebViewClient(new WebViewClient());//不跳转浏览器
        var settings = mWebview.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(false);//禁用新窗口
        settings.setJavaScriptEnabled(true);//允许执行js
        settings.setLoadWithOverviewMode(true);//启用自适应屏幕
        settings.setSupportZoom(false);//禁用缩放
        settings.setBuiltInZoomControls(false);//禁用显示缩放按钮
        settings.setUseWideViewPort(false);//禁用大视图模式
        settings.setDatabaseEnabled(false);//是否使用缓存
        settings.setDomStorageEnabled(true);//DOM存储
        var data = getOneData();//打开网站
        mWebview.loadUrl(data.getWeburl());
        //让系统知道有菜单项
        setHasOptionsMenu(true);
        //返回视图
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_browser, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        var view = getView();
        var context = view.getContext();
        var id = item.getItemId();
        if (id == R.id.browser_btnGetCK) {//获取按钮
            //显示出该对话框
            StaticObj.dialogLoading = DialogLoading.build(context);
            StaticObj.dialogLoading.show();
            CookieHep.getCookie(context, null, handler);
            return true;
        } else if (id == R.id.browser_btnRefresh) {//刷新按钮
            var mWebview = (WebView) view.findViewById(R.id.my_webview);
            mWebview.clearHistory();//清除历史数据
            mWebview.clearFormData();//清除表单数据
            mWebview.clearCache(true);//清除缓存数据
            var data = getOneData();
            mWebview.loadUrl(data.getWeburl());
            return true;
        }
        //其它让基类来处理
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //获取第一条数据
    private WebData getOneData() {
        var data = SqliteHelp.GetWebData();
        if (data.getWeburl().isEmpty()) {
            var url = "http://zgcwkj.cn/";
            data.setWeburl(url);
            var id = "00000000-0000-0000-0000-000000000000";
            data.setId(id);
        }
        return data;
    }
}
