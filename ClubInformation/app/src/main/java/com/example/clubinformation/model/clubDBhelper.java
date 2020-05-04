package com.example.clubinformation.model;
//创建数据库，，，，，固定形式
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class clubDBhelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "club.db";
    private static final String SQL_CREATE_ENTRIES =
            "create table "+"user_table"+"("
                    +"ID"+" INTEGER primary key,"
                    +"user_Password "+"text,"
                    +"user_Name"+" text)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + "user_table";
    private static final String SQL_CREATE_CLUB =
            "create table "+"Information_table"+"("
                    +"ID"+" INTEGER primary key,"
                    +"title "+"text,"
                    +"content"+" Memo,"
                    +"time"+" text,"
                    +" userID"+" text)";
    private static final String SQL_DELETE_CLUB =
            "DROP TABLE IF EXISTS " + "user_table";
    public clubDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_CLUB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_CLUB);
        onCreate(db);
    }
}