package com.zgcwkj.bllcode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;

import com.zgcwkj.getcks.StaticObj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonHelp {

    //复制到剪切板
    public static void copyToClipboard(Context context, String text, Handler handler) {
        text = text.trim();
        var clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        var clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        //发送消息
        if (handler != null) {
            StaticObj.sendMsg(handler, 1);
        }
    }

    //获取Data目录
    public static String getDataPath(Context context) {
        var dataDir = context.getDataDir();
        var appDataDirPath = dataDir.getAbsolutePath();
        return appDataDirPath;
    }

    //复制文件
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            var oldFile = new File(oldPath);
            var oldFileIs = new FileInputStream(oldFile);
            var newFile = new File(newPath);
            var deleteCkOk = newFile.delete();//删除目标位置的文件
            var newFileOs = new FileOutputStream(newFile);
            var buffer = new byte[1024];
            int length;
            while ((length = oldFileIs.read(buffer)) > 0) {
                newFileOs.write(buffer, 0, length);
            }
            if (oldFileIs != null) oldFileIs.close();
            if (newFileOs != null) newFileOs.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //删除文件夹（递归）
    public static boolean deleteFiles(File folder) {
        if (folder != null && folder.isDirectory()) {
            var files = folder.listFiles();
            if (files != null) {
                for (var file : files) {
                    if (file.isDirectory()) {
                        //递归调用删除子文件夹
                        deleteFiles(file);
                    } else {
                        file.delete();
                    }
                }
            }
            return folder.delete();
        }
        return false;
    }

    //获取MD5值
    public static String getMd5(String value) {
        var valueMd5 = "zgcwkj";
        try {
            var md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            valueMd5 = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
        }
        return valueMd5;
    }
}
