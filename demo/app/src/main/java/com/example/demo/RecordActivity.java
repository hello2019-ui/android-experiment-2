package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.bean.NotepadBean;
import com.example.demo.database.SQLiteHelper;
import com.example.demo.utils.DBUtils;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView note_back;
    TextView note_time;
    String note_author;
    EditText title;
    EditText content;
    ImageView delete;
    ImageView note_save;
    TextView noteName;;
    private SQLiteHelper mSQLiteHelper;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        note_back=(ImageView)findViewById(R.id.note_back);//后退键
        note_time=(TextView)findViewById(R.id.tv_time);//保存记录的时间
        note_author=Login.username;//记录作者
        title=(EditText)findViewById(R.id.note_title);//记录标题
        content=(EditText) findViewById(R.id.note_content);//记录的内容
        delete=(ImageView)findViewById(R.id.delete);//清空的按钮
        note_save=(ImageView)findViewById(R.id.note_save);//保存的按钮
        noteName=(TextView) findViewById(R.id.note_name);//标题的名称
        note_back.setOnClickListener(this);
        delete.setOnClickListener(this);
        note_save.setOnClickListener(this);
        initData();

    }

    //增或改
    public void initData(){
        mSQLiteHelper=new SQLiteHelper(this);
        noteName.setText("添加记录");
        Intent intent=getIntent();
        if(intent!=null){
            id=intent.getStringExtra("id");
            //已经存在时，从"增"变为"改"
            if(id!=null){
                noteName.setText("修改记录");
                title.setText(intent.getStringExtra("title"));
                content.setText(intent.getStringExtra("content"));
                note_time.setText(intent.getStringExtra("time"));
                note_time.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){

            //返回键
            case R.id.note_back:
                finish();
                break;

            //按删除键
            case R.id.delete:
                title.setText(" ");
                content.setText(" ");
                break;

            //保存键
            case R.id.note_save:
                String noteTitle = title.getText().toString().trim();
                String noteContent = content.getText().toString().trim();
                if(id!=null){
                    //修改记录的功能
                    if(noteContent.length()>0){
                        if (mSQLiteHelper.updateData(id,noteTitle,noteContent,DBUtils.getTime())){
                            showToast("修改成功");
                            setResult(2);
                            finish();
                        }else{
                            showToast("修改失败");
                        }
                    } else{
                        showToast("修改的记录内容不能为空");
                    }
                }else{
                    //添加记录的功能
                    if(noteContent.length()>0){
                        if (mSQLiteHelper.insertData(noteTitle,noteContent,DBUtils.getTime())){
                            showToast("保存成功");
                            setResult(2);
                            finish();
                        }else{
                            showToast("保存失败");
                        }
                    } else{
                        showToast("保存的记录内容不能为空");
                    }
                }
                break;
        }
    }
    public void showToast(String message){
        Toast.makeText(RecordActivity.this,message, Toast.LENGTH_LONG).show();
    }
}
