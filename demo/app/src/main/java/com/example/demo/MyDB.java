package com.example.demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDB extends SQLiteOpenHelper {
    //用户表
    public static  final  String CREATE_USER="create table tb_user(" +
            "user_id integer primary key autoincrement," +
            "username varchar(50)," +
            "password varchar(50))";

    //上下文
    private  Context mcontext;
    public static final  String CREATE_NOTES="";
    public MyDB(Context context) {
        super(context, "MyDB.db", null, 2);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        Toast.makeText(mcontext,"创建成功！",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
