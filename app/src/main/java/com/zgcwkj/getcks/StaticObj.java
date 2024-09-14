package com.zgcwkj.getcks;

import android.os.Handler;

import com.zgcwkj.bllcode.DialogLoading;
import com.zgcwkj.getcks.dialogs.QLConfigDialog;
import com.zgcwkj.getcks.dialogs.WebDataDialog;

//静态对象
public class StaticObj {
    //加载弹窗
    public static DialogLoading dialogLoading;

    //输入信息弹窗
    public static WebDataDialog dialogInput;

    //输入信息弹窗2
    public static QLConfigDialog dialogInputQl;

    //发送消息
    public static void sendMsg(Handler handler, int msgWhat) {
        var iMsg = handler.obtainMessage();
        iMsg.what = msgWhat;
        handler.sendMessage(iMsg);
    }
}