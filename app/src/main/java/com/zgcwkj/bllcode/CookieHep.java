package com.zgcwkj.bllcode;

import android.content.Context;
import android.os.Handler;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.zgcwkj.getcks.StaticObj;
import com.zgcwkj.models.WebData;

import java.io.File;
import java.util.ArrayList;

public class CookieHep {
    //获取 Cookie
    public static String getCookie(Context context, WebData data, Handler handler) {
        var isCopy = false;
        if (data == null || data.getId().isEmpty()) {
            isCopy = true;
            data = SqliteHelp.GetWebData();
        }
        //浏览器的CK
        var cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        var cookieStr = cookieManager.getCookie(data.getWeburl());
        if (cookieStr == null) cookieStr = "";
        if (cookieStr.isEmpty() && !isCopy) return "";
        var cookies = cookieStr.split(";");
        //准备数据
        var getCookieKey = data.getCookiekey();
        if (!getCookieKey.equals("")) {
            var cookieStrBuilder = new StringBuilder();
            //要取出匹配Cookie的值
            var cookieKeys = getCookieKey.split(";");
            for (var cookieKey : cookieKeys) {//要匹配的Key
                cookieKey = cookieKey.trim();//前后去空格
                if (cookieKey.isEmpty()) continue;
                var matched = false;//记录是否被匹配到
                for (var item : cookies) {//存储的CK
                    item = item.trim();//前后去空格
                    if (item.isEmpty()) continue;
                    var leftKey = item.split("=");
                    if (leftKey.length > 0 && leftKey[0].equals(cookieKey)) {
                        cookieStrBuilder.append(item).append(";");
                        matched = true;
                        break;
                    }
                }
                //匹配不到时，直接存储到要推送的记录里
                if (!matched) {
                    cookieStrBuilder.append(cookieKey).append(";");
                }
            }
            cookieStr = cookieStrBuilder.toString();
        }
        //清理两端空格
        cookieStr = cookieStr.trim();
        //是否复制
        if (isCopy) {
            //将内容复制到剪切板
            if (!cookieStr.isEmpty()) {
                //显示
                StaticObj.dialogLoading = DialogLoading.build(context);
                StaticObj.dialogLoading.show();
                var qlData = QLongHelp.getData(context);
                if (qlData.getWeburl().isEmpty()) {
                    //传到剪切板
                    CommonHelp.copyToClipboard(context, cookieStr, handler);
                } else {
                    //传到青龙
                    QLongHelp.saveCookie(context, qlData, cookieStr, data.getRemark(), handler);
                }
            } else {
                Toast.makeText(context, "内容为空", Toast.LENGTH_SHORT).show();
            }
        }
        //返回状态
        return cookieStr;
    }

    //设置Cookie
    public static boolean setCookie(Context context, WebData data) {
        //选中新数据
        data.setIsselect(true);
        SqliteHelp.SaveWebData(data);
        //返回状态
        return true;
    }

    //清楚无关的缓存
    public static boolean clearHistory(Context context) {
        var dataPath = CommonHelp.getDataPath(context);
        var keyList = new ArrayList<String>();
        var dataList = SqliteHelp.GetWebDatas();
        for (var item : dataList) {
            var dataKey = CommonHelp.getMd5(item.getId());
            keyList.add("app_webview_" + dataKey);
        }
        var dataDir = new File(dataPath);
        if (dataDir.exists() && dataDir.isDirectory()) {
            // 遍历数据目录
            var files = dataDir.listFiles();
            if (files == null) return false;
            for (var file : files) {
                if (file.isDirectory()) {
                    var fileName = file.getName();
                    if (fileName.contains("app_webview_")) {
                        if (!keyList.contains(file.getName())) {
                            //递归删除文件夹
                            CommonHelp.deleteFiles(file);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
