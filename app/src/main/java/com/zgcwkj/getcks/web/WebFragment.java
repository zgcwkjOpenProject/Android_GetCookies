package com.zgcwkj.getcks.web;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.zgcwkj.bllcode.SqliteHelp;
import com.zgcwkj.getcks.R;
import com.zgcwkj.getcks.StaticObj;
import com.zgcwkj.getcks.dialogs.QLConfigDialog;
import com.zgcwkj.getcks.dialogs.WebDataDialog;

public class WebFragment extends Fragment {
    public static WebHandler handler;//消息传递
    public WebViewModel viewModel;//页面模型

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var context = this.getContext();
        //传递消息
        handler = new WebHandler(Looper.myLooper(), this);
        //视图模型
        viewModel = new ViewModelProvider(this).get(WebViewModel.class);
        //视图
        var view = inflater.inflate(R.layout.fragment_web, container, false);
        //加载列表数据
        LoadDataListView(view);
        //让系统知道有菜单项
        setHasOptionsMenu(true);
        //返回视图
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_web, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        var view = getView();
        var context = getContext();
        var id = item.getItemId();
        if (id == R.id.web_btnAdd) {//添加按钮
            StaticObj.dialogInput = WebDataDialog.build(context);
            StaticObj.dialogInput.show(handler);
            return true;
        } else if (id == R.id.web_btnQl) {//配置青龙
            StaticObj.dialogInputQl = QLConfigDialog.build(context);
            StaticObj.dialogInputQl.show(handler);
            return true;
        }
        //其它让基类来处理
        return super.onOptionsItemSelected(item);
    }

    /**
     * 加载列表数据
     */
    public void LoadDataListView(View view) {
        //绑定 ListView 数据
        var dataList = SqliteHelp.GetWebDatas();
        var sDAdapter = new WebDataAdapter(this, R.layout.web_listview_data, dataList, handler);
        var listView = (ListView) view.findViewById(R.id.server_data);
        listView.setAdapter(sDAdapter);
    }
}
