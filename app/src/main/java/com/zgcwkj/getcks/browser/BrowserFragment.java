package com.zgcwkj.getcks.browser;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.zgcwkj.bllcode.CommonHelp;
import com.zgcwkj.bllcode.CookieHep;
import com.zgcwkj.bllcode.QLongHelp;
import com.zgcwkj.bllcode.SqliteHelp;
import com.zgcwkj.getcks.MainActivity;
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
        openWebUrl(view, false);
        //让系统知道有菜单项
        setHasOptionsMenu(true);
        //返回视图
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_browser, menu);
        //动态变换菜单按钮文字
        var qlData = QLongHelp.getData(getContext());
        var item = menu.findItem(R.id.browser_btnGetCK);
        if (qlData.getWeburl().isEmpty()) {
            item.setTitle(R.string.menu_getCK1);
        } else {
            item.setTitle(R.string.menu_getCK2);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        var view = getView();
        var context = view.getContext();
        var id = item.getItemId();
        if (id == R.id.browser_btnGetCK) {//获取按钮
            CookieHep.getCookie(context, null, handler);
            return true;
        } else if (id == R.id.web_btnResetBrowser) {//重置浏览器按钮
            var builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("确定重置浏览器吗？");
            //设置按钮事件
            builder.setPositiveButton("确定", (dialog, which) -> {
                CookieManager.getInstance().removeAllCookies(null);//清除CK
                var mWebview = (WebView) view.findViewById(R.id.my_webview);
                mWebview.clearCache(true);//清除缓存数据
                mWebview.clearHistory();//清除历史数据
                mWebview.clearFormData();//清除表单数据
                mWebview.reload();
            });
            //设置按钮事件
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
            //显示对话框
            builder.show();
            return true;
        } else if (id == R.id.web_btnDesktopBrowser) {//桌面版
            var mWebview = (WebView) view.findViewById(R.id.my_webview);
            var newUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0";
            mWebview.getSettings().setUserAgentString(newUA);
            mWebview.getSettings().setUseWideViewPort(true);
            mWebview.getSettings().setLoadWithOverviewMode(true);
            mWebview.reload();
            return true;
        } else if (id == R.id.web_btnMobileBrowser) {//手机版
            var mWebview = (WebView) view.findViewById(R.id.my_webview);
            mWebview.getSettings().setUserAgentString(null);
            mWebview.getSettings().setUseWideViewPort(false);
            mWebview.getSettings().setLoadWithOverviewMode(false);
            mWebview.reload();
            return true;
        } else if (id == R.id.browser_btnRefresh) {//刷新按钮
            openWebUrl(view, true);
            return true;
        }
        //其它让基类来处理
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //打开网站（第一条数据）
    private WebData openWebUrl(View view, boolean isLoad) {
        var data = SqliteHelp.GetWebData();
        if (data.getWeburl().isEmpty()) {
            var url = "http://zgcwkj.cn/";
            data.setWeburl(url);
            var id = "00000000-0000-0000-0000-000000000000";
            data.setId(id);
        }
        //检查是否需要重启
        var dataKey = CommonHelp.getMd5(data.getId());
        if ((data.getCookieIsolate() && !dataKey.equals(StaticObj.dataDirectorySuffix)) ||
                (!data.getCookieIsolate() && !StaticObj.dataDirectorySuffix.isEmpty())) {
            //显示对话框
            var builder = new AlertDialog.Builder(view.getContext());
            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("检测到环境变化，请点击重启应用！");
            builder.setPositiveButton("重启", (dialog, which) -> {
                MainActivity.activity.restartApp();
            });
            builder.show();
        }
        //浏览器
        var mWebview = (WebView) view.findViewById(R.id.my_webview);
        //配置浏览器客户端
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager.getInstance().flush();//存储 Cookie
            }
        });
        //刷新按钮时，清理缓存
        if (isLoad) {
            mWebview.clearHistory();//清除历史数据
            mWebview.clearFormData();//清除表单数据
            mWebview.clearCache(true);//清除缓存数据
        }
        var settings = mWebview.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(false);//禁用新窗口
        settings.setJavaScriptEnabled(true);//允许执行js
        settings.setLoadWithOverviewMode(true);//启用自适应屏幕
        settings.setSupportZoom(false);//禁用缩放
        settings.setBuiltInZoomControls(false);//禁用显示缩放按钮
        settings.setUseWideViewPort(false);//禁用大视图模式
        settings.setDatabaseEnabled(false);//是否使用缓存
        settings.setDomStorageEnabled(true);//DOM存储
        //打开网站
        mWebview.loadUrl(data.getWeburl());
        return data;
    }
}
