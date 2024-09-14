package com.zgcwkj.getcks.browser;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.zgcwkj.getcks.StaticObj;

import java.lang.ref.WeakReference;

public class BrowserHandler extends Handler {
    WeakReference<BrowserFragment> myActivity;

    public BrowserHandler(@NonNull Looper looper, BrowserFragment activity) {
        super(looper);//调用父类的显式指明的构造函数
        myActivity = new WeakReference(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        var nactivity = myActivity.get();
        if (nactivity == null) return;
        Context context = nactivity.getContext();
        //关闭加载弹窗
        StaticObj.dialogLoading.close();
        switch (msg.what) {
            case 0:
                Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(context, "内容已复制到剪切板", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(context, "失败，青龙配置异常", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(context, "内容已传输到青龙", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
