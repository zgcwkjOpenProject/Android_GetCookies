package com.zgcwkj.getcks.web;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.zgcwkj.getcks.StaticObj;

import java.lang.ref.WeakReference;

public class WebHandler extends Handler {
    WeakReference<WebFragment> myActivity;

    public WebHandler(@NonNull Looper looper, WebFragment activity) {
        super(looper);//调用父类的显式指明的构造函数
        myActivity = new WeakReference(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        var nactivity = myActivity.get();
        if (nactivity == null) return;
        Context context = nactivity.getContext();
        //
        StaticObj.dialogLoading.close();
        switch (msg.what) {
            case 0:
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                StaticObj.dialogInput.close();//关闭对话框
                nactivity.LoadDataListView(nactivity.getView());//加载列表数据
                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(context, "选择失败", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                nactivity.LoadDataListView(nactivity.getView());//加载列表数据
                Toast.makeText(context, "选择成功", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                StaticObj.dialogInputQl.close();//关闭对话框
                nactivity.LoadDataListView(nactivity.getView());//加载列表数据
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
