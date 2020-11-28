package com.example.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.demo.bean.NotepadBean;
import com.example.demo.R;

import java.util.List;

public class NotepadAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;//这个对象用于加载item的布局文件
    private List<NotepadBean> list;//list集合是列表中需要显示的集合
    public NotepadAdapter(Context context, List<NotepadBean> list){
        layoutInflater= LayoutInflater.from(context);
        this.list=list;
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }//获取集合长度

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }//返回集合的内容

    @Override
    public long getItemId(int position) {
        return position;
    }//返回位置信息

    /*
    getview方法
    ViewHolder作用：ListView滚动时快速设置值，而不必每次重新创建很多对象
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){//不存在ViewHolder对象
            //加载Item界面对应的布局文件
            convertView=layoutInflater.inflate(R.layout.notepad_item,null);
            viewHolder=new ViewHolder(convertView);//创建ViewHolder
            convertView.setTag(viewHolder);//关联ViewHolder对象
        }else {
            viewHolder=(ViewHolder) convertView.getTag();//关联ViewHolder对象
        }
        NotepadBean notepadBean=(NotepadBean)getItem(position);
        viewHolder.tvNotepadTitle.setText(notepadBean.getNotepadTitle());
        viewHolder.tvNotepadContent.setText(notepadBean.getNotepadContent());
        viewHolder.tvNotepadTime.setText(notepadBean.getNotepadTime());
        viewHolder.tvNotepadAuthor.setText(notepadBean.getNotepadAuthor());
        return convertView;
    }
    class ViewHolder{
        TextView tvNotepadTitle;
        TextView tvNotepadContent;
        TextView tvNotepadTime;
        TextView tvNotepadAuthor;
        public ViewHolder(View view){
            tvNotepadTitle=view.findViewById(R.id.item_title);//记录的标题
            tvNotepadContent=view.findViewById(R.id.item_content);//记录的内容
            tvNotepadTime=view.findViewById(R.id.item_time);//保存记录的时间
            tvNotepadAuthor=view.findViewById(R.id.item_author);//记录的作者

        }
    }
}
