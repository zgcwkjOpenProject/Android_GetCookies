package com.zgcwkj.bllcode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zgcwkj.models.WebData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SqliteHelp extends SQLiteOpenHelper {
    private static SqliteHelp sqliteHelp;

    public SqliteHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //获取数据库
    public static SqliteHelp getDb(Context context) {
        if (sqliteHelp == null) {
            sqliteHelp = new SqliteHelp(context, "web_app.db", null, 1);
        }
        sqliteHelp.getReadableDatabase();
        return sqliteHelp;
    }

    //获取记录
    public static List<WebData> GetWebDatas() {
        SQLiteDatabase db = sqliteHelp.getReadableDatabase();
        if (!db.isOpen()) return new ArrayList<>();
        var cursor = db.rawQuery("select * from web_data", null);
        var dataList = getCursorWebData(cursor);
        return dataList;
    }

    //获取记录
    public static WebData GetWebData() {
        var data = new WebData(true, "", "", "", "");
        var db = sqliteHelp.getReadableDatabase();
        if (!db.isOpen()) return data;
        var cursor = db.rawQuery("select * from web_data where isselect = 1", null);
        var dataList = getCursorWebData(cursor);
        data = dataList.get(0);
        return data;
    }

    //获取网站数据
    private static List<WebData> getCursorWebData(Cursor cursor) {
        List<WebData> dataList = new ArrayList<>();
        while (cursor.moveToNext()) {
            var idIndex = cursor.getColumnIndex("id");
            var id = cursor.getString(idIndex);
            var weburlIndex = cursor.getColumnIndex("weburl");
            var weburl = cursor.getString(weburlIndex);
            var cookieIndex = cursor.getColumnIndex("cookie");
            var cookie = cursor.getString(cookieIndex);
            var cookiekeyIndex = cursor.getColumnIndex("cookiekey");
            var cookiekey = cursor.getString(cookiekeyIndex);
            var isselectIndex = cursor.getColumnIndex("isselect");
            var isselect = cursor.getInt(isselectIndex) > 0;
            var remarkIndex = cursor.getColumnIndex("remark");
            var remark = cursor.getString(remarkIndex);
            dataList.add(new WebData(isselect, weburl, cookie, cookiekey, remark, id));
        }
        return dataList;
    }

    //保存记录
    public static void SaveWebData(WebData data) {
        var db = sqliteHelp.getWritableDatabase();
        if (!db.isOpen()) return;
        //参数
        var values = new ContentValues();
        values.put("weburl", data.getWeburl());
        values.put("cookie", data.getCookie());
        values.put("cookiekey", data.getCookiekey());
        values.put("remark", data.getRemark());
        values.put("isselect", data.getIsselect());
        if (!ExistWebData(data)) {
            //新增
            var id = UUID.randomUUID().toString();
            data.setId(id);
            values.put("id", data.getId());
            db.insert("web_data", null, values);
        } else {
            //修改
            db.update("web_data", values, "id = @id", new String[]{data.getId()});
        }
        //选择记录
        if (data.getIsselect()) {
            //全部变成未选择
            db.execSQL("update web_data set isselect = 0");
            //标记选择的
            db.execSQL("update web_data set isselect = 1 where id =  @id", new String[]{data.getId()});
        }
    }

    //删除记录
    public static void DelWebData(WebData data) {
        var db = sqliteHelp.getWritableDatabase();
        if (!db.isOpen()) return;
        db.execSQL("delete from web_data where id = @id", new Object[]{data.getId()});
    }

    //是否存在记录
    public static boolean ExistWebData(WebData data) {
        var db = sqliteHelp.getReadableDatabase();
        if (!db.isOpen()) return false;
        var cursor = db.rawQuery("select * from web_data where id = @id", new String[]{data.getId()});
        return cursor.getCount() > 0;
    }

    //初始化数据时创建数据
    public static void initDBData(WebData data, SQLiteDatabase db) {
        var sql = "insert into web_data(id,weburl,cookie,cookiekey,remark,isselect) values(@id,@weburl,@cookie,@cookiekey,@remark,@isselect)";
        var id = UUID.randomUUID().toString();
        db.execSQL(sql, new Object[]{id, data.getWeburl(), data.getCookie(), data.getCookiekey(), data.getRemark(), data.getIsselect()});
    }

    //初始化数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        //表结构
        var sql = "CREATE TABLE web_data (id TEXT PRIMARY KEY,weburl TEXT,cookie TEXT,cookiekey TEXT,remark TEXT,isselect BOOLEAN);";
        db.execSQL(sql);
        //默认数据
        var data1 = new WebData(true, "http://zgcwkj.cn", "", "ck1;ck2;", "示例");
        initDBData(data1, db);
        //默认数据
        var data2 = new WebData(false, "https://github.com/zgcwkjOpenProject/Android_GetCookies", "", "", "开源地址");
        initDBData(data2, db);
        //默认数据
        var data3 = new WebData(false, "https://plogin.m.jd.com/login/login", "", "pt_pin;pt_key;", "京东CK");
        initDBData(data3, db);
        //默认数据
        var data4 = new WebData(false, "https://h5.ele.me/login", "", "", "饿了么CK");
        initDBData(data4, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
