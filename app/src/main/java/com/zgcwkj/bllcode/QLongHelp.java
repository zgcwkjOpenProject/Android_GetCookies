package com.zgcwkj.bllcode;

import android.content.Context;

import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.OkHttps;
import com.ejlchina.okhttps.gson.GsonMsgConvertor;
import com.google.gson.Gson;
import com.zgcwkj.models.QLData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class QLongHelp {

    //获取青龙配置
    public static QLData getData(Context context) {
        try {
            var dataPath = CommonHelp.getDataPath(context);
            var file = new File(dataPath + "/qlData.json");
            var json = new StringBuilder();
            var br = new BufferedReader(new FileReader(file));
            var line = "";
            while ((line = br.readLine()) != null) {
                json.append(line);
                json.append('\n');
            }
            var data = new Gson().fromJson(json.toString(), QLData.class);
            return data;
        } catch (Exception e) {
        }
        return new QLData();
    }

    //保存青龙配置
    public static boolean setData(Context context, QLData data) {
        try {
            var dataPath = CommonHelp.getDataPath(context);
            var file = new File(dataPath + "/qlData.json");
            var json = new Gson().toJson(data);
            var writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //清空青龙配置
    public static boolean clearData(Context context) {
        var dataPath = CommonHelp.getDataPath(context);
        var file = new File(dataPath + "/qlData.json");
        return file.delete();
    }

    //保存到青龙平台
    public static void saveCookie(Context context, QLData qlData, String ck, String remark) {
        new Thread(() -> {
            //获取Token
            var http = HTTP.builder().addMsgConvertor(new GsonMsgConvertor()).build();
            var url = qlData.getWeburl() + "/open/auth/token?";
            url += "client_id=" + qlData.getClientId() + "&client_secret=" + qlData.getClientSecret();
            var data1 = http.sync(url).get().getBody().toMapper();
            if (data1.getInt("code") != 200) return;
            var token = data1.getMapper("data").getString("token");
            //从记录中获取匹配备注的记录
            url = qlData.getWeburl() + "/open/envs";
            var data2 = http.sync(url).addHeader("Authorization", "Bearer " + token).get().getBody().toMapper();
            if (data2.getInt("code") != 200) return;
            var envId = 0;
            var envName = "NotName";
            var envList = data2.getArray("data");
            for (var i = 0; i < envList.size(); i++) {
                var envData = envList.getMapper(i);
                var remarks = envData.getString("remarks");
                if (remarks != null && remarks.equals(remark)) {
                    envId = envData.getInt("id");
                    envName = envData.getString("name");
                    break;
                }
            }
            //把记录提交到青龙
            if (envId == 0) {
                //新增
                var saveData = "[{'value':'" + ck + "','name':'" + envName + "','remarks':'" + remark + "'}]";
                saveData = saveData.replaceAll("'", "\"");
                var rdata = http.async(url).addHeader("Authorization", "Bearer " + token).bodyType(OkHttps.JSON).setBodyPara(saveData).post();
            } else {
                //修改
                var saveData = "{'value':'" + ck + "','name':'" + envName + "','remarks':'" + remark + "','id':" + envId + "}";
                saveData = saveData.replaceAll("'", "\"");
                var rdata = http.async(url).addHeader("Authorization", "Bearer " + token).bodyType(OkHttps.JSON).setBodyPara(saveData).put();
            }
        }).start();
    }
}
