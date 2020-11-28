package com.example.demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.Login;
import com.example.demo.bean.NotepadBean;
import com.example.demo.utils.DBUtils;

public class SQLiteHelper extends SQLiteOpenHelper {
    private SQLiteDatabase sqLiteDatabase;

    //创建数据库
    public SQLiteHelper(Context context){
        super(context, DBUtils.DATABASE_NAME,null,DBUtils.DATABASE_VERION);
        sqLiteDatabase=this.getWritableDatabase();
    }

    //创建表 create table xx (id intrger primary key autoincrement, title text, content text, notetime text, author text)
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+DBUtils.DATABASE_TABLE+"("+DBUtils.NOTEPAD_ID+
                " INTEGER PRIMARY KEY AUTOINCREMENT,"+DBUtils.NOTEPAD_TITLE+" text, "+DBUtils.NOTEPAD_CONTENT+" text, "
                +DBUtils.NOTEPAD_TIME+" text,"+DBUtils.NOTEPAD_AUTHOR+" String)");
    }

    //升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    //添加数据
    public boolean insertData(String userTitlt, String userContent, String userTime){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_TITLE,userTitlt);
        contentValues.put(DBUtils.NOTEPAD_CONTENT,userContent);
        contentValues.put(DBUtils.NOTEPAD_TIME,userTime);
        contentValues.put(DBUtils.NOTEPAD_AUTHOR, Login.username);
        return sqLiteDatabase.insert(DBUtils.DATABASE_TABLE,null,contentValues)>0;
    }

    //删除数据
    public boolean deleteData(String id){
        String sql=DBUtils.NOTEPAD_ID+"=?";
        //连接字符串，易错点!
        String[] contentValuesArray=new String[]{String.valueOf(id)};
        return sqLiteDatabase.delete(DBUtils.DATABASE_TABLE,sql,contentValuesArray)>0;
    }

    //修改数据
    public boolean updateData(String id,String title, String content, String userYear){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_TITLE,title);
        contentValues.put(DBUtils.NOTEPAD_CONTENT,content);
        contentValues.put(DBUtils.NOTEPAD_TIME,userYear);
        String sql=DBUtils.NOTEPAD_ID+"=?";
        String[] strings=new String[]{id};
        return sqLiteDatabase.update(DBUtils.DATABASE_TABLE,contentValues,sql,strings)>0;
    }
    //查询数据
    public List<NotepadBean> query(){
        List<NotepadBean> list=new ArrayList<NotepadBean>();
        Cursor cursor=sqLiteDatabase.query(DBUtils.DATABASE_TABLE,null, "author ='"+Login.username+"'",
                null,null,null,DBUtils.NOTEPAD_ID+" desc");
        if (cursor!=null){
            while (cursor.moveToNext()){
                NotepadBean noteInfo=new NotepadBean();
                String id= String.valueOf(cursor.getInt(cursor.getColumnIndex(DBUtils.NOTEPAD_ID)));
                String title=cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_TITLE));
                String content=cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_CONTENT));
                String time=cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_TIME));
                String author=cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_AUTHOR));
                noteInfo.setId(id);
                noteInfo.setNotepadTitle(title);
                noteInfo.setNotepadContent(content);
                noteInfo.setNotepadTime(time);
                noteInfo.setNotepadAuthor(author);
                list.add(noteInfo);
            }
            cursor.close();
        }
        return list;
    }
}
