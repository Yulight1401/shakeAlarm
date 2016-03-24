package com.lvyteam.shakealarm;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 亮 on 2016/3/24.
 */
public class AlarmSetDialog extends Dialog implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener{
    private  SetRepeatListener setRepeatListener;
    private  SetDeleteListener setDeleteListener;
    private  EveryDayListener evertdaylistener;
    private Map<String,Object> map;
    private Map<String,Object> map2;

    private  int postion;
    private  int i=0;
    Context context;

public interface EveryDayListener{
    public void everyday(int postion);
}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        i++;
         map = new HashMap<String, Object>();
//        if(i==1){
//            map.put("postion",postion);
//        }
        switch (buttonView.getId()){

            case R.id.everyday:
                evertdaylistener.everyday(postion);
                break;
            case R.id.day1:
                if(buttonView.isChecked()){
                    map.put("day1",true);
                }
                else {
                    map.remove("day1");
                    map.put("day1",false);
                }
                break;
            case R.id.day2:
                if(buttonView.isChecked()){
                    map.put("day2",true);
                }
                else {
                    map.remove("day2");
                    map.put("day2", false);
                }
                break;
            case R.id.day3:
                if(buttonView.isChecked()){
                    map.put("day3",true);
                }
                else {
                    map.remove("day3");
                    map.put("day3", false);
                }
                break;
            case R.id.day4:
                if(buttonView.isChecked()){
                    map.put("day4",true);
                }
                else {
                    map.remove("day4");
                    map.put("day4", false);
                }
                break;
            case R.id.day5:
                if(buttonView.isChecked()){
                    map.put("day5",true);
                }
                else {
                    map.remove("day5");
                    map.put("day5", false);
                }
                break;
            case R.id.day6:
                if(buttonView.isChecked()){
                    map.put("day6",true);
                }
                else {
                    map.remove("day6");
                    map.put("day6", false);
                }
                break;
            case R.id.day7:
                if(buttonView.isChecked()){
                    map.put("day7",true);
                }
                else {
                    map.remove("day7");
                    map.put("day7", false);
                }
                break;

        }

        setRepeatListener.setrepeat(map);
    }

    public  interface SetDeleteListener{
        public void delete(int postion);
    }
    public interface SetRepeatListener{
        public void setrepeat(Map<String,Object> data);
    }
    public AlarmSetDialog(Context context) {
        super(context);
    }

    public AlarmSetDialog(Context context, int theme,SetRepeatListener setRepeatListener,SetDeleteListener setDeleteListener,EveryDayListener everydaylistener,int postion) {
        super(context, theme);
        this.evertdaylistener=everydaylistener;
        this.postion=postion;
        this.context=context;
        this.setRepeatListener=setRepeatListener;
        this.setDeleteListener=setDeleteListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        i=0;
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.alarmsetdialog);


        CheckBox ringeveryday=(CheckBox)findViewById(R.id.everyday);
        CheckBox day1=(CheckBox)findViewById(R.id.day1);
        CheckBox day2=(CheckBox)findViewById(R.id.day2);
        CheckBox day3=(CheckBox)findViewById(R.id.day3);
        CheckBox day4=(CheckBox)findViewById(R.id.day4);
        CheckBox day5=(CheckBox)findViewById(R.id.day5);
        CheckBox day6=(CheckBox)findViewById(R.id.day6);
        CheckBox day7=(CheckBox)findViewById(R.id.day7);
        day1.setOnCheckedChangeListener(this);
        day2.setOnCheckedChangeListener(this);
        day3.setOnCheckedChangeListener(this);
        day4.setOnCheckedChangeListener(this);
        day5.setOnCheckedChangeListener(this);
        day6.setOnCheckedChangeListener(this);
        day7.setOnCheckedChangeListener(this);
        ringeveryday.setOnCheckedChangeListener(this);

        SharedPreferences sp=context.getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE);
        String getcontent =sp.getString(String.valueOf(postion) + "repeat", null);
        if (getcontent==null){

        }
        else {
            String[] contentstring=getcontent.split(",");
            for(int i=0;i<contentstring.length;i=i+2){
                switch (contentstring[0+i]){
                    case "day1":
                        if(Boolean.valueOf(contentstring[1+i]))
                        {
                            day1.setChecked(true);
                        }
                        else day1.setChecked(false);
                        Log.d("211","21213");
                        break;
                    case "day2":
                        if(Boolean.valueOf(contentstring[1+i]))
                        {
                            day2.setChecked(true);
                        }
                        else day2.setChecked(false);
                        Log.d("211","21213");
                        break;
                    case "day3":
                        if(Boolean.valueOf(contentstring[1+i]))
                        {
                            day3.setChecked(true);
                        }
                        else day3.setChecked(false);
                        Log.d("211","21213");
                        break;
                    case "day4":
                        if(Boolean.valueOf(contentstring[1+i]))
                        {
                            day4.setChecked(true);
                        }
                        else day4.setChecked(false);
                        Log.d("211","21213");
                        break;
                    case "day5":
                        if(Boolean.valueOf(contentstring[1+i]))
                        {
                            day5.setChecked(true);
                        }
                        else day5.setChecked(false);
                        Log.d("211","21213");
                        break;
                    case "day6":
                        if(Boolean.valueOf(contentstring[1+i]))
                        {
                            day6.setChecked(true);
                        }
                        else day6.setChecked(false);
                        Log.d("211","21213");
                        break;
                    case "day7":
                        if(Boolean.valueOf(contentstring[1+i]))
                        {
                            day7.setChecked(true);
                        }
                        else day7.setChecked(false);
                        Log.d("211","21213");
                        break;
                }
            }
        }



        Button deletebutton=(Button)findViewById(R.id.deletebutton);


        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeleteListener.delete(postion);
                dismiss();
            }
        });

    }

    protected AlarmSetDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }


    @Override
    public void onClick(View v) {

    }
}
