package com.zgcwkj.bllcode;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zgcwkj.getcks.R;

/**
 * 加载层
 */
public class DialogLoading {
    private final Context mContext;
    public boolean start = false;
    private AlertDialog dialog;

    public DialogLoading(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取一个单例
     */
    public static DialogLoading build(Context mContext) {
        var dialogLoading = new DialogLoading(mContext);
        return dialogLoading;
    }

    /**
     * 显示
     */
    public void show() {
        //防止重复弹出
        if (start) return;
        start = true;
        //弹窗
        var llPadding = 30;
        var ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);
        //
        var progressBar = new ProgressBar(mContext);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);
        //
        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        var tvText = new TextView(mContext);
        tvText.setText(R.string.appLoading);
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);
        //
        ll.addView(progressBar);
        ll.addView(tvText);
        //
        var builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setView(ll);
        //捕获按钮事件
        builder.setOnKeyListener((dialogInterface, keyCode, keyEvent) -> {
            return true;
        });
        //
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        var window = dialog.getWindow();
        if (window != null) {
            var layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }

    /**
     * 关闭
     */
    public void close() {
        //防止重复弹出
        start = false;
        //关闭
        dialog.dismiss();
    }
}
