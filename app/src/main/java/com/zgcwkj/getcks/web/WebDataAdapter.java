package com.zgcwkj.getcks.web;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zgcwkj.bllcode.CookieHep;
import com.zgcwkj.bllcode.DialogLoading;
import com.zgcwkj.bllcode.SqliteHelp;
import com.zgcwkj.getcks.R;
import com.zgcwkj.getcks.StaticObj;
import com.zgcwkj.getcks.dialogs.WebDataDialog;
import com.zgcwkj.models.WebData;

import java.util.List;

public class WebDataAdapter extends ArrayAdapter<WebData> {
    private final WebFragment myFragment;//上下文
    private final Handler handler;//上下文

    public WebDataAdapter(WebFragment serverFragment, int textViewResourceID, List<WebData> objects, Handler handler) {
        super(serverFragment.getContext(), textViewResourceID, objects);
        this.myFragment = serverFragment;
        this.handler = handler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var view = LayoutInflater.from(getContext()).inflate(R.layout.web_listview_data, null);
        var webData = getItem(position);
        //网址
        var tv_weburl = (TextView) view.findViewById(R.id.web_url);
        tv_weburl.setText(webData.getWeburl());
        //备注
        var tv_remark = (TextView) view.findViewById(R.id.web_remark);
        tv_remark.setText(webData.getRemark());
        //选择网站
        var ll_listview = (LinearLayout) view.findViewById(R.id.web_listview);
        ll_listview.setOnClickListener(view1 -> onClickListview(view1, webData));
        //编辑数据
        var b_edit = (Button) view.findViewById(R.id.web_btn_edit);
        b_edit.setOnClickListener(view1 -> onClickEdit(view1, webData));
        //删除
        var b_delete = (Button) view.findViewById(R.id.web_btn_delete);
        b_delete.setOnClickListener(view1 -> onClickDelete(view1, webData));
        //选择状态
        var linearLayout = (LinearLayout) view.findViewById(R.id.web_select);
        if (webData.getIsselect()) {
            linearLayout.setBackgroundColor(Color.YELLOW);
            b_delete.setVisibility(View.INVISIBLE);
        }
        //返回结构
        return view;
    }

    /**
     * 列表单击
     *
     * @param view1
     * @param data
     */
    public void onClickListview(View view1, WebData data) {
        var view = myFragment.getView();
        var context = myFragment.getContext();
        CookieHep.setCookie(context, data);//设置CK
        //反馈消息至界面
        StaticObj.dialogLoading = DialogLoading.build(context);
        StaticObj.dialogLoading.show();
        StaticObj.sendMsg(handler, 4);
    }

    /**
     * 编辑数据
     *
     * @param view1
     * @param data
     */
    private void onClickEdit(View view1, WebData data) {
        var view = myFragment.getView();
        var context = myFragment.getContext();
        StaticObj.dialogInput = WebDataDialog.build(context);
        StaticObj.dialogInput.show(data, handler);
    }

    /**
     * 删除数据
     *
     * @param view1
     * @param data
     */
    private void onClickDelete(View view1, WebData data) {
        var view = myFragment.getView();
        var context = myFragment.getContext();
        //通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        var builder = new AlertDialog.Builder(context);
        //设置Title的图标
        builder.setIcon(R.drawable.ic_launcher);
        //设置Title的内容
        builder.setTitle("确定删除吗？");
        //设置显示的内容
        builder.setMessage("地址：" + data.getWeburl() + "\r\n" + "备注：" + data.getRemark());
        //设置按钮事件
        builder.setPositiveButton("确定", (dialog, which) -> {
            SqliteHelp.DelWebData(data);
            myFragment.LoadDataListView(view);//加载列表数据
        });
        //设置按钮事件
        builder.setNegativeButton("取消", (dialog, which) -> {

        });
        //显示出该对话框
        builder.show();
    }
}
