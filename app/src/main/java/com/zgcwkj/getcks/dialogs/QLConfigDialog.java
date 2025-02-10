package com.zgcwkj.getcks.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.zgcwkj.bllcode.DialogLoading;
import com.zgcwkj.bllcode.QLongHelp;
import com.zgcwkj.getcks.R;
import com.zgcwkj.getcks.StaticObj;

public class QLConfigDialog {
    private Context mContext;//上下文
    private AlertDialog dialog;//弹窗

    private QLConfigDialog() {
    }

    /**
     * 获取一个单例
     */
    public static QLConfigDialog build(Context mContext) {
        StaticObj.dialogLoading = DialogLoading.build(mContext);
        var dialogInput = new QLConfigDialog();
        dialogInput.mContext = mContext;
        return dialogInput;
    }

    /**
     * 显示
     */
    public void show(Handler handler) {
        var data = QLongHelp.getData(mContext);
        //加载布局
        final var contentView = View.inflate(mContext, R.layout.web_input_qldata, null);
        //青龙平台地址
        var qlurl = (TextView) contentView.findViewById(R.id.web_inputData_qlurl);
        qlurl.setText(data.getWeburl());
        //客户端ID
        var qlClientId = (TextView) contentView.findViewById(R.id.web_inputData_qlClientId);
        qlClientId.setText(data.getClientId());
        //客户端密钥
        var qlClientSecret = (TextView) contentView.findViewById(R.id.web_inputData_qlClientSecret);
        qlClientSecret.setText(data.getClientSecret());
        //自动启动变量
        var qlAutoEnable = (Switch) contentView.findViewById(R.id.web_inputData_qlAutoEnable);
        qlAutoEnable.setChecked(data.getAutoEnable());
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
            StaticObj.dialogLoading.show();
            if (!qlurl.getText().toString().trim().isEmpty()) {
                data.setWeburl(qlurl.getText().toString());
                data.setClientId(qlClientId.getText().toString());
                data.setClientSecret(qlClientSecret.getText().toString());
                data.setAutoEnable(qlAutoEnable.isChecked());
                QLongHelp.setData(mContext, data);
                isOK = true;
            }
            //发送消息
            StaticObj.sendMsg(handler, (isOK ? 11 : 0));
        });
        //清空按钮事件
        btnClear.setOnClickListener(arg -> {
            StaticObj.dialogLoading.show();
            //清空数据
            var isOK = QLongHelp.clearData(mContext);
            isOK = true;
            //发送消息
            StaticObj.sendMsg(handler, (isOK ? 11 : 0));
            //关闭弹窗
            close();
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
