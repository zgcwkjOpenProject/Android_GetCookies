package com.zgcwkj.getcks.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
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
        var data = new WebData(false);
        show(data, handler);
    }

    //显示
    public void show(WebData data, Handler handler) {
        //加载布局
        final var contentView = View.inflate(mContext, R.layout.web_input_data, null);
        var context = contentView.getContext();
        //Url
        var ipWeburl = (TextView) contentView.findViewById(R.id.web_inputData_weburl);
        ipWeburl.setText(data.getWeburl());
        //CKK
        var ipCookiekey = (TextView) contentView.findViewById(R.id.web_inputData_cookiekey);
        ipCookiekey.setText(data.getCookiekey());
        //CKKData
        if (!data.getWeburl().isEmpty() && data.getIsselect()) {
            //CK
            var ipCookie = (TextView) contentView.findViewById(R.id.web_inputData_cookie);
            var getCKK = CookieHep.getCookie(mContext, data, null);
            ipCookie.setText(getCKK);
        }
        //用户代理
        var ipUserAgent = (TextView) contentView.findViewById(R.id.web_inputData_userAgent);
        ipUserAgent.setText(data.getUserAgent());
        //备注
        var ipRemark = (TextView) contentView.findViewById(R.id.web_inputData_remark);
        ipRemark.setText(data.getRemark());
        //数据隔离
        var ipCookieIsolate = (Switch) contentView.findViewById(R.id.web_inputData_cookieIsolate);
        ipCookieIsolate.setChecked(data.getCookieIsolate());
        //不支持的系统版本隐藏，安卓9
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            ipCookieIsolate.setVisibility(View.GONE);
        }
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
            if (!ipWeburl.getText().toString().trim().isEmpty()
                    && !ipRemark.getText().toString().isEmpty()) {
                data.setWeburl(ipWeburl.getText().toString());
                data.setCookiekey(ipCookiekey.getText().toString());
                data.setUserAgent(ipUserAgent.getText().toString());
                data.setRemark(ipRemark.getText().toString());
                data.setCookieIsolate(ipCookieIsolate.isChecked());
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
