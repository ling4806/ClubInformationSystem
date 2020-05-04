package com.example.clubinformation;
//对数据库的操作增删改查
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.clubinformation.model.clubDBhelper;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class clubcontent {
    // 定义数据库
    private static SQLiteDatabase db;
    // 定义联系人列表
    public List<InformationItem> items = new ArrayList<InformationItem>();
    //    定义上下文
    private Context context;

    public clubcontent(Context context) {
        this.context = context;
        queryDatabase();
    }

    public static InformationItem insertData(String title, String content, String   time, String  userID) {
        InformationItem item = null;
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("time", time);
        values.put("userID",  userID);
        long flag = db.insert("Information_table", null, values);
        if (flag > 0) {
            item = new InformationItem(String.valueOf(flag),title,content,time,userID);
        }
        return item;
    }

    public static InformationItem updateData(String  id,String title, String content, String   time, String  userID) {
        InformationItem item = null;
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("time", time);
        values.put("userID",  userID);
        String selection = "ID" + " = ?";
        String[] selectionArgs = {id};
        int flag = db.update("Information_table", values, selection, selectionArgs);
        if (flag > 0) {
            item = new InformationItem(id,title,content,time,userID);
        }
        return item;
    }

    public static int deleteData(String id) {
        String selection = "ID" + "= ? ";
        String[] selectionArgs = {id};
        int deletedRows = db.delete("Information_table", selection, selectionArgs);
        return deletedRows;
    }

    public List<InformationItem> getItems() {
        return items;
    }

    //查询数据库数据函数
    private void queryDatabase() {
        clubDBhelper dbhelper = new clubDBhelper(context);
        db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query("Information_table", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                String  userID = cursor.getString(cursor.getColumnIndex("userID"));
                long item_id = cursor.getInt(cursor.getColumnIndex("ID" ));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                InformationItem item = new InformationItem(String.valueOf(item_id),title,content,time,userID);
//               放入定义好的联系人列表
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    // 定义联系人实体------内部类-----序列化
    public static class InformationItem   implements Serializable {
        public final String  ID;
        public String title;    //标题
        public String content;     //内容
        public String   time;     //内容
        public String  userID;     //内容
        public InformationItem(String  id,String title, String content, String   time, String  userID) {
            this. ID = id;
            this.title = title;
            this.content = content;
            this.time = time;
            this.userID = userID;
        }
    }
}

