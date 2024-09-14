package com.zgcwkj.getcks.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zgcwkj.bllcode.DialogLoading;
import com.zgcwkj.bllcode.QLongHelp;
import com.zgcwkj.getcks.R;

public class QLConfigDialog {
    private Context mContext;//上下文
    private DialogLoading loading;//加载弹窗
    private AlertDialog dialog;//弹窗

    private QLConfigDialog() {
    }

    /**
     * 获取一个单例
     */
    public static QLConfigDialog build(Context mContext, DialogLoading dialogLoading) {
        var dialogInput = new QLConfigDialog();
        dialogInput.mContext = mContext;
        dialogInput.loading = dialogLoading;
        return dialogInput;
    }

    /**
     * 显示
     */
    public void show(Handler handler) {
        var data = QLongHelp.getData(mContext);
        //加载布局
        final var contentView = View.inflate(mContext, R.layout.web_input_qldata, null);
        //Url
        var tv_qlurl = (TextView) contentView.findViewById(R.id.web_inputData_qlurl);
        tv_qlurl.setText(data.getWeburl());
        //ClientId
        var tv_qlClientId = (TextView) contentView.findViewById(R.id.web_inputData_qlClientId);
        tv_qlClientId.setText(data.getClientId());
        //ClientSecret
        var tv_qlClientSecret = (TextView) contentView.findViewById(R.id.web_inputData_qlClientSecret);
        tv_qlClientSecret.setText(data.getClientSecret());
        //按钮
        var btnOk = (Button) contentView.findViewById(R.id.web_inputData_btnQlOk);
        var btnClear = (Button) contentView.findViewById(R.id.web_inputData_btnQlClear);
        var btnCancel = (Button) contentView.findViewById(R.id.web_inputData_btnQlCancel);
        //通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        var builder = new AlertDialog.Builder(mContext);
        //设置View来输入框
        builder.setView(contentView);
        //显示出该对话框
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //确定按钮事件
        btnOk.setOnClickListener(arg -> {
            var isOK = false;
            loading.show();
            if (!tv_qlurl.getText().toString().trim().isEmpty()) {
                data.setWeburl(tv_qlurl.getText().toString());
                data.setClientId(tv_qlClientId.getText().toString());
                data.setClientSecret(tv_qlClientSecret.getText().toString());
                QLongHelp.setData(mContext, data);
                isOK = true;
            }
            //发送消息
            var iMsg = handler.obtainMessage();
            iMsg.what = isOK ? 5 : 0;
            handler.sendMessage(iMsg);
        });
        //清空按钮事件
        btnClear.setOnClickListener(arg -> {
            loading.show();
            //清空数据
            var isOK = QLongHelp.clearData(mContext);
            //发送消息
            var iMsg = handler.obtainMessage();
            iMsg.what = isOK ? 5 : 0;
            handler.sendMessage(iMsg);
        });
        //取消按钮事件
        btnCancel.setOnClickListener(arg -> {
            close();
        });
    }

    /**
     * 关闭
     */
    public void close() {
        dialog.dismiss();
    }
}
