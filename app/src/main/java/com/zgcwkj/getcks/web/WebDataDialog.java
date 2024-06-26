package com.zgcwkj.getcks.web;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zgcwkj.bllcode.CookieHep;
import com.zgcwkj.bllcode.DialogLoading;
import com.zgcwkj.bllcode.SqliteHelp;
import com.zgcwkj.getcks.R;
import com.zgcwkj.models.WebData;

public class WebDataDialog {
    //上下文
    private Context mContext;
    //加载弹窗
    private DialogLoading loading;
    //消息对象
    private WebHandler handler;
    //弹窗
    private AlertDialog dialog;

    private WebDataDialog() {
    }

    /**
     * 获取一个单例
     */
    public static WebDataDialog build(Context mContext, WebHandler handler, DialogLoading dialogLoading) {
        var dialogInput = new WebDataDialog();
        dialogInput.mContext = mContext;
        dialogInput.handler = handler;
        dialogInput.loading = dialogLoading;
        return dialogInput;
    }

    /**
     * 显示
     */
    public void show() {
        var data = new WebData(false, "", "", "", "", "");
        show(data);
    }

    /**
     * 显示
     */
    public void show(WebData data) {
        //加载布局
        final var contentView = View.inflate(mContext, R.layout.web_input_data, null);
        var context = contentView.getContext();
        //Url
        var tv_weburl = (TextView) contentView.findViewById(R.id.web_inputData_weburl);
        tv_weburl.setText(data.getWeburl());
        //CKK
        var tv_cookiekey = (TextView) contentView.findViewById(R.id.web_inputData_cookiekey);
        tv_cookiekey.setText(data.getCookiekey());
        //CKKData
        if (!data.getWeburl().isEmpty() && data.getIsselect()) {
            //CK
            var tv_cookie = (TextView) contentView.findViewById(R.id.web_inputData_cookie);
            var getCKK = CookieHep.getCookie(mContext, data);
            tv_cookie.setText(getCKK);
        }
        //备注
        var tv_remark = (TextView) contentView.findViewById(R.id.web_inputData_remark);
        tv_remark.setText(data.getRemark());
        //按钮
        var btnOk = (Button) contentView.findViewById(R.id.web_inputData_btnOk);
        var btnCancel = (Button) contentView.findViewById(R.id.web_inputData_btnCancel);
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
            if (!tv_weburl.getText().toString().trim().isEmpty()) {
                data.setWeburl(tv_weburl.getText().toString());
                data.setCookiekey(tv_cookiekey.getText().toString());
                data.setRemark(tv_remark.getText().toString());
                SqliteHelp.SaveWebData(data);
                isOK = true;
            }
            //发送消息
            var iMsg = handler.obtainMessage();
            iMsg.what = isOK ? 1 : 0;
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
