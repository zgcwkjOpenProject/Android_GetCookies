package com.zgcwkj.bllcode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CommonHelp {

    //复制到剪切板
    public static void copyToClipboard(Context context, String text) {
        text = text.trim();
        var clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        var clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
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
                    file.delete();//删除文件或子文件夹
                }
            }
            return folder.delete();//删除空文件夹或目录
        }
        return false;
    }
}
