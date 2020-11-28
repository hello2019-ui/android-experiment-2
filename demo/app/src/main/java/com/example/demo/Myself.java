package com.example.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Myself extends AppCompatActivity implements View.OnClickListener{

    EditText edt_name;
    EditText edt_behavior;
    EditText edt_mail;
    EditText edt_age;
    EditText edt_talk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        edt_name=(EditText)findViewById(R.id.edt_my_name); //名字
        edt_behavior=findViewById(R.id.edt_behavior);//爱好
        edt_mail = findViewById(R.id.edt_mail);//邮箱
        edt_age = findViewById(R.id.edt_age);//年龄
        edt_talk = findViewById(R.id.edt_talk);//座右铭

        ImageView save = findViewById(R.id.author_save);//保存键
        ImageView back=(ImageView)findViewById(R.id.note_back);//后退键
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        initData();
    }

    //增或改
    public void initData(){
        Intent intent=getIntent();
        SharedPreferences check = getSharedPreferences("author"+Login.username, MODE_PRIVATE);
        if (intent != null) {
            edt_name.setText(check.getString("name",""));
            edt_age.setText(check.getString("age",""));
            edt_mail.setText(check.getString("mail",""));
            edt_behavior.setText(check.getString("behavior",""));
            edt_talk.setText(check.getString("talk",""));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            //返回键
            case R.id.note_back:
                finish();
                break;

            case R.id.author_save:
                String name = edt_name.getText().toString().trim();
                String age = edt_age.getText().toString().trim();
                String mail = edt_mail.getText().toString().trim();
                String behavior = edt_behavior.getText().toString().trim();
                String talk = edt_talk.getText().toString().trim();
                SharedPreferences.Editor editor = getSharedPreferences("author"+Login.username, MODE_PRIVATE).edit();
                editor.putString("name",name);
                editor.putString("age",age);
                editor.putString("mail",mail);
                editor.putString("behavior",behavior);
                editor.putString("talk",talk);
                editor.apply();
                Toast.makeText(Myself.this,"保存成功", Toast.LENGTH_LONG).show();
                break;
        }
    }

}
