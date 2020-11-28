package com.example.demo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    public static int userID;
    public static String username;
    String name, password;//保存数据
    private EditText edt_username;//用户名字
    private EditText edt_password;//用户密码
    private Button btn_login;
    private Button btn_go_register;
    //private SQLiteDatabase sqlDate;
    private MyDB myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        myDB = new MyDB(this);//创建对象
        //注册
        btn_go_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Regist.class);
                startActivityForResult(intent,1);
            }
        });
        //登入
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edt_username.getText().toString();
                password = edt_password.getText().toString();
                if (enter(name, password)) {
                    Toast.makeText(Login.this, "登录成功，" + name, Toast.LENGTH_SHORT).show();
                    new Thread(){
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                Intent intent1 = new Intent(Login.this, NotepadActivity.class);
                                startActivity(intent1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) && TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "登入失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 初始化：initUI()
     */
    private void initUI() {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_go_register = (Button) findViewById(R.id.btn_go_register);
    }
    /**
     * 判断是否username,password 和数据库的相同 enter()
     */
    public boolean enter(String name, String password) {
        SQLiteDatabase db = myDB.getWritableDatabase();
        String sql = "select user_id from tb_user where username=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[]{name, password});
        if (cursor.getCount() != 0) {//返回Cursor 中的行数
            if (cursor.moveToNext()) {
                userID = cursor.getInt(cursor.getColumnIndex("user_id"));
                username = name;
            }
            cursor.close();
            return true;
        }
        return false;
    }
    /**
     * 数据回显
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==1){
            String name=data.getStringExtra("name");
            String password=data.getStringExtra("password");
            edt_username.setText(name);
            edt_password.setText(password);
        }
    }
}
