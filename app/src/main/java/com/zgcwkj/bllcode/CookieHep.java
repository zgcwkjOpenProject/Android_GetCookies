package com.zgcwkj.bllcode;

import android.content.Context;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.zgcwkj.models.WebData;

public class CookieHep {
    //获取 Cookie
    public static String getCookie(Context context, WebData data) {
        var isCopy = false;
        if (data == null || data.getId().isEmpty()) {
            isCopy = true;
            data = SqliteHelp.GetWebData();
        }
        var cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        var cookieStr = cookieManager.getCookie(data.getWeburl());
        if (cookieStr == null) return "";
        var cookies = cookieStr.split(";");
        //准备数据
        var getCookieKey = data.getCookiekey();
        if (!getCookieKey.equals("")) {
            cookieStr = "";
            var cookieKeys = getCookieKey.split(";");
            for (var item : cookies) {
                if (item.isEmpty()) continue;
                for (var cookieKey : cookieKeys) {
                    if (cookieKey.isEmpty()) continue;
                    if (item.contains(cookieKey)) {
                        cookieStr += item + ";";
                    }
                }
            }
        }
        //清理两端空格
        cookieStr = cookieStr.trim();
        //是否复制
        if (isCopy) {
            //将内容复制到剪切板
            if (!cookieStr.isEmpty()) {
                var qlData = QLongHelp.getData(context);
                if (qlData.getWeburl().isEmpty()) {
                    //传到剪切板
                    CommonHelp.copyToClipboard(context, cookieStr);
                    Toast.makeText(context, "内容已复制到剪切板", Toast.LENGTH_SHORT).show();
                } else {
                    //传到青龙
                    QLongHelp.saveCookie(context, qlData, cookieStr, data.getRemark());
                    Toast.makeText(context, "内容已传输到青龙", Toast.LENGTH_SHORT).show();
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
        //var dataPath = CommonHelp.getDataPath(context);
        //var cookiePath = dataPath + "/app_webview/Default/Cookies";
        ////存放 Cookie 文件的文件夹
        //var destFile = new File(dataPath + "/Cookies");
        //if (!destFile.exists()) destFile.mkdirs();
        ////在用的 Cookie
        //var oldData = SqliteHelp.GetWebData();
        //var oldCkPath = dataPath + "/Cookies/" + oldData.getId();
        //var copyOldCkOk = CommonHelp.copyFile(cookiePath, oldCkPath);
        ////新的 Cookie
        //var newCkPath = dataPath + "/Cookies/" + data.getId();
        //var copyNewCkOk = CommonHelp.copyFile(newCkPath, cookiePath);
        ////当新的没有 Cookie 时，删除现有文件
        //if (copyOldCkOk && !copyNewCkOk) {
        //    var cookieFile = new File(cookiePath);
        //    cookieFile.delete();
        //}
        //选中新数据
        data.setIsselect(true);
        SqliteHelp.SaveWebData(data);
        //返回状态
        return true;
    }
}
