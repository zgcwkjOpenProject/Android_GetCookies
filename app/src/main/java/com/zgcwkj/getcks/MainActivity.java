package com.zgcwkj.getcks;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zgcwkj.bllcode.CommonHelp;
import com.zgcwkj.bllcode.CookieHep;
import com.zgcwkj.bllcode.SqliteHelp;

public class MainActivity extends AppCompatActivity {
    public static MainActivity activity;
    private Context mContext;
    private long exitTime = 0;//退出计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        mContext = getApplicationContext();
        //数据库
        SqliteHelp.initDb(this);
        //隔离浏览器数据
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //清除无用的历史数据
            CookieHep.clearHistory(mContext);
            //创建隔离环境
            var data = SqliteHelp.GetWebData();
            if (data.getCookieIsolate()) {
                var dataKey = CommonHelp.getMd5(data.getId());
                WebView.setDataDirectorySuffix(dataKey);
                StaticObj.dataDirectorySuffix = dataKey;
            }
        }
        //允许浏览器调试
        WebView.setWebContentsDebuggingEnabled(true);
        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        var navView = (BottomNavigationView) findViewById(R.id.nav_view);
        //将每个菜单ID作为一组ID传递，因为每个菜单应被视为顶级目的地
        var appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_browser, R.id.navigation_web).build();
        var navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    //监听按键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按钮事件
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        //执行原有方法
        return super.onKeyDown(keyCode, event);
    }

    //重启应用
    public void restartApp() {
        final var intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //杀掉以前进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
