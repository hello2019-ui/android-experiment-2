package com.example.demo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Regist extends AppCompatActivity {
    private EditText edt_setusername;
    private EditText edt_setpassword;
    private EditText edt_confirm_password;
    private Button btn_register;
    String name, password, id;
    private MyDB myDB;
    ContentValues contentValues = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
        initUI();
        edt_setpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edt_confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        myDB = new MyDB(this);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = myDB.getWritableDatabase();
                name = edt_setusername.getText().toString();
                password = edt_setpassword.getText().toString();
                if (edt_setusername.getText().toString().contains(" ") || edt_setpassword.getText().toString().contains(" ")) {
                    Toast.makeText(Regist.this, "输入的用户名或密码不能包含空格", Toast.LENGTH_SHORT).show();
                    return;
                } else if (edt_setpassword.getText().toString().length() == 0 || edt_setusername.getText().toString().length() == 0) {
                    Toast.makeText(Regist.this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!edt_confirm_password.getText().toString().equals(edt_setpassword.getText().toString())) {
                    Toast.makeText(Regist.this, "两次密码不一致，请确认再注册", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (login(name)) {
                    Toast.makeText(Regist.this, "该用户名已注册", Toast.LENGTH_SHORT).show();
                    return;
                }
                contentValues.put("username", name);
                contentValues.put("password", password);
                db.insert("tb_user", null, contentValues);
                String sql = "select user_id from tb_user where username=? and password=?";
                Cursor cursor = db.rawQuery(sql, new String[]{name, password});
                if (cursor.moveToNext()) {//id
                    id = Integer.toString(cursor.getInt(0));//防止数据库的值null和0 分不清
                }
                cursor.close();
                db.close();
                Toast.makeText(Regist.this, "注册成功", Toast.LENGTH_SHORT).show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(1000);
                            Intent intent = new Intent(Regist.this, Login.class);
                            intent.putExtra("name", edt_setusername.getText().toString());
                            intent.putExtra("password", edt_setpassword.getText().toString());
                            setResult(1, intent);//返回页面1
                            finish();
                            //  startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    /**
     * 初始化View :initUI()
     */
    public void initUI() {
        edt_setusername = findViewById(R.id.edt_setusername);
        edt_setpassword = findViewById(R.id.edt_setpassword);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_register = findViewById(R.id.btn_register);
    }

    /**
     * 查询数据库是否有重复的
     * 进行数据的查询   login()
     */
    public boolean login(String name) {
        SQLiteDatabase db = myDB.getWritableDatabase();
        String sql = "select * from tb_user where username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        if (cursor.getCount() != 0) {
            cursor.close();
            return true;
        } else
            return false;
    }
}