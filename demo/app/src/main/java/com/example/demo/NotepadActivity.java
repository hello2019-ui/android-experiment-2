package com.example.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import com.example.demo.adapter.NotepadAdapter;
import com.example.demo.bean.NotepadBean;
import com.example.demo.database.SQLiteHelper;

public class NotepadActivity extends AppCompatActivity {
    private ListView listView;
    private SQLiteHelper mSQLiteHelper;
    private List<NotepadBean> list;
    NotepadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listview);
        Button imageView=findViewById(R.id.add);
        Button author=findViewById(R.id.btn_change_author);
        Button exit=findViewById(R.id.btn_exit);
        initData();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotepadActivity.this,RecordActivity.class);
                startActivityForResult(intent,1);
            }
        });
        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotepadActivity.this, Myself.class);
                startActivityForResult(intent,1);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotepadActivity.this, Login.class);
                startActivityForResult(intent,1);
            }
        });
    }
    //列表显示
    public void initData(){
        mSQLiteHelper=new SQLiteHelper(this);
        showQueryData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id ){
                NotepadBean notepadBean=list.get(position);
                Intent intent=new Intent(NotepadActivity.this,RecordActivity.class);
                intent.putExtra("id",notepadBean.getId());
                intent.putExtra("title",notepadBean.getNotepadTitle());
                intent.putExtra("content",notepadBean.getNotepadContent());
                intent.putExtra("time",notepadBean.getNotepadTime());
                intent.putExtra("author",notepadBean.getNotepadAuthor());
                NotepadActivity.this.startActivityForResult(intent,1);
            }
        });

        //长按删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder=new AlertDialog.Builder(NotepadActivity.this)
                        .setMessage("是否删除此记录?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotepadBean notepadBean=list.get(position);
                                if(mSQLiteHelper.deleteData(notepadBean.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(NotepadActivity.this,"删除成功", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog=builder.create();
                dialog.show();
                return true;
            }
    });
}

    //显示该作者所有日志
    private void showQueryData(){
        if(list!=null){
            list.clear();
        }
        list=mSQLiteHelper.query();
        adapter=new NotepadAdapter(this,list);
        listView.setAdapter(adapter);
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1&&resultCode==2){
            showQueryData();
        }
    }
}
