package com.lvyteam.shakealarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by 亮 on 2016/3/21.
 */
public class MyListAdapter extends BaseAdapter {
    private List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public MyListAdapter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);

    }
    //添加

    /*组件*/
    public final  class Parts {
        public Switch sw;
        public TextView tx;

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Parts parts=null;
        if(convertView==null){
            parts=new Parts();
            convertView=layoutInflater.inflate(R.layout.listitem,null);
            parts.tx=(TextView)convertView.findViewById(R.id.timetitle);
            parts.sw=(Switch)convertView.findViewById(R.id.alarmswich);
            convertView.setTag(parts);


        }
        else
        {
            parts=(Parts)convertView.getTag();
        }
        parts.tx.setText((String)data.get(position).get("title"));
        parts.sw.setChecked((Boolean)data.get(position).get("check"));
        return convertView;
    }
}
