package com.zgcwkj.getcks.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zgcwkj.bllcode.CookieHep;
import com.zgcwkj.bllcode.DialogLoading;
import com.zgcwkj.bllcode.SqliteHelp;
import com.zgcwkj.getcks.R;
import com.zgcwkj.getcks.StaticObj;
import com.zgcwkj.models.WebData;

public class WebDataDialog {
    private Context mContext;//上下文
    private AlertDialog dialog;//弹窗

    private WebDataDialog() {
    }

    //获取一个单例
    public static WebDataDialog build(Context mContext) {
        StaticObj.dialogLoading = DialogLoading.build(mContext);
        var dialogInput = new WebDataDialog();
        dialogInput.mContext = mContext;
        return dialogInput;
    }

    //显示
    public void show(Handler handler) {
        var data = new WebData(false, "", "", "", "", "");
        show(data, handler);
    }

    //显示
    public void show(WebData data, Handler handler) {
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
            var getCKK = CookieHep.getCookie(mContext, data, null);
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
            StaticObj.dialogLoading.show();
            if (!tv_weburl.getText().toString().trim().isEmpty()
                    && !tv_remark.getText().toString().isEmpty()) {
                data.setWeburl(tv_weburl.getText().toString());
                data.setCookiekey(tv_cookiekey.getText().toString());
                data.setRemark(tv_remark.getText().toString());
                SqliteHelp.SaveWebData(data);
                isOK = true;
            }
            //发送消息
            StaticObj.sendMsg(handler, (isOK ? 1 : 0));
        });
        //取消按钮事件
        btnCancel.setOnClickListener(arg -> {
            close();
        });
    }

    //关闭
    public void close() {
        dialog.dismiss();
    }
}
