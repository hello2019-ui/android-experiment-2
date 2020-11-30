package com.example.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private Button btn_nobody;
    private CheckBox rem_pwd;
    private CheckBox show_pwd;
    private boolean is_rem;
    private SharedPreferences pref;//定义一个SharedPreferences对象
    private SharedPreferences.Editor editor;//调用SharedPreferences对象的edit()方法来获取一个SharedPreferences.Editor对象，用以添加要保存的数据

    //private SQLiteDatabase sqlDate;
    private MyDB myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        myDB = new MyDB(this);//创建对象

        //获取上一次"是否保存密码"
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        is_rem = pref.getBoolean("save_password",false);
        if(is_rem){
            //从SharedPreferences中获取保存密码用户的用户名与密码
            String user = pref.getString("account","");
            String password = pref.getString("password","");
            edt_username.setText(user);
            edt_password.setText(password);
            rem_pwd.setChecked(true);
        }
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
                                Login.this.finish();
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

        //用户点击'显示密码'复选框
        show_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_pwd.isChecked()) {
                    showOrHide(edt_password, true);
                } else {
                    showOrHide(edt_password, false);
                }
            }
        });
    }

    //当用户离开活动时，检测是否勾选记住密码，若勾选则保存用户输入的用户名及密码
    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor = pref.edit();
        String account = edt_username.getText().toString();
        String password = edt_password.getText().toString();
        if (rem_pwd.isChecked()) {
            editor.putBoolean("save_password", true);
            editor.putString("account", account);
            editor.putString("password", password);
        } else {
            editor.clear();
        }
        editor.apply();
    }

    /**
     * 初始化：initUI()
     */
    private void initUI() {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_go_register = (Button) findViewById(R.id.btn_go_register);
        //btn_nobody = (Button)findViewById(R.id.btn_nobody);
        rem_pwd = (CheckBox)findViewById(R.id.rem_pwd);
        show_pwd = (CheckBox)findViewById(R.id.show_pwd);

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
    //显示或隐藏密码
    private void showOrHide(EditText passwordEdit, boolean isShow) {

        //记住光标开始的位置
        int pos = passwordEdit.getSelectionStart();
        if (isShow) {
            passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        passwordEdit.setSelection(pos);
    }
}
