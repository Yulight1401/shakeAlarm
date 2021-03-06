package com.lvyteam.shakealarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by 亮 on 2016/3/21.
 */
public class MyListAdapter extends BaseAdapter {

    private List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private  long time;
    private SetAlarmListener setAlarmListener;
    private  CancelAlarmListener cancelAlarmListener;
    private  ChangeTimeListener changeTimeListener;

    public  interface  ChangeTimeListener{
        public void ChangeTime(int postion);
    }
 public interface  SetAlarmListener{
       public void SetAlarm(int postion);
    }
    public  interface CancelAlarmListener{
        public  void CancelAlarm(int postion);

    }

    public MyListAdapter(Context context, List<Map<String, Object>> data,SetAlarmListener setAlarmListener,CancelAlarmListener cancelAlarmListener,ChangeTimeListener changeTimeListener){
        this.setAlarmListener=setAlarmListener;
        this.changeTimeListener=changeTimeListener;
        this.cancelAlarmListener=cancelAlarmListener;
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);

    }
    //添加
    public void refresh(List<Map<String,Object>> data2){
        this.data=data2;
        notifyDataSetChanged();

    }


    /*组件*/
    public final  class Parts {
        public Switch sw;
        public TextView tx;

    }
    public void settime(long time){
        this.time =time;
    }

    public List<Map<String,Object>> getdata(){
        return this.data;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Parts parts=null;
        if(convertView==null){
            parts=new Parts();
            convertView=layoutInflater.inflate(R.layout.listitem,null);
            parts.tx=(TextView)convertView.findViewById(R.id.timetitle);
            parts.sw=(Switch)convertView.findViewById(R.id.alarmswich);
            convertView.setTag(parts);
           parts.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if (isChecked) {
                       //开启闹钟
                   } else {

                       cancelAlarmListener.CancelAlarm(position);
                       //取消闹钟
                   }
               }
           });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTimeListener.ChangeTime(position);
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setAlarmListener.SetAlarm(position);
                    Log.d("positon::::::::",String.valueOf(position));
//                    new AlertDialog.Builder(context).setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            switch (which) {
//                                case 0:
//
//                                    break;
//                                default:
//                                    break;
//                            }
//
//                        }
//                    }).setNegativeButton("取消", null).show();
                    return true;
                }
            });

        }
        else
        {
            parts=(Parts)convertView.getTag();
        }
        parts.tx.setText((String) data.get(position).get("title"));
        parts.sw.setChecked((Boolean) data.get(position).get("check"));



        return convertView;
    }
}
