package com.example.clubinformation;
//对数据库的操作增删改查
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.clubinformation.model.clubDBhelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class usercontent {
    // 定义数据库
    public static SQLiteDatabase db;
    // 定义联系人列表
    public List<USerItem> items = new ArrayList<USerItem>();
    //    定义上下文
    private Context context;

    public usercontent(Context context) {
        this.context = context;
        queryDatabase();
    }

    public static USerItem insertData(String name, String Password) {
        USerItem item = null;
        ContentValues values = new ContentValues();
        values.put("user_Name", name);
        values.put("user_Password", Password);
        long flag = db.insert("user_table", null, values);
        if (flag > 0) {
            item = new USerItem(String.valueOf(flag), name, Password);
        }
        return item;
    }

    public static USerItem updateData(String id, String name, String Password) {
        USerItem item = null;
        ContentValues values = new ContentValues();
        values.put("user_Name", name);
        values.put("user_Password", Password);
        String selection = "ID" + " = ?";
        String[] selectionArgs = {id};
        int flag = db.update("user_table", values, selection, selectionArgs);
        if (flag > 0) {
            item = new USerItem(id, name, Password);
        }
        return item;
    }

    public static int deleteData(String id) {
        String selection = "ID" + "= ? ";
        String[] selectionArgs = {id};
        int deletedRows = db.delete("user_table", selection, selectionArgs);
        return deletedRows;
    }

    public List<USerItem> getItems() {
        return items;
    }

    //查询数据库数据函数
    private void queryDatabase() {
        clubDBhelper dbhelper = new clubDBhelper(context);
        db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query("user_table", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long item_id = cursor.getInt(cursor.getColumnIndex("ID" ));
                String name = cursor.getString(cursor.getColumnIndex("user_Name"));
                String Password = cursor.getString(cursor.getColumnIndex("user_Password"));
                USerItem item = new USerItem(String.valueOf(item_id), name, Password);
//               放入定义好的联系人列表
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    // 定义联系人实体------内部类-----序列化
    public static class USerItem  {
        public final String id;
        public String name;    //联系人姓名
        public String phoneNumber;     //联系人电话号码

        public USerItem(String id, String name, String phoneNumber) {
            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;
        }
    }
}



