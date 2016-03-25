package com.lvyteam.shakealarm;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private List<Map<String, Object>> data;
    private AlarmManager alarmManager;
    private MyListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        alarmManager=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        data =readdata();
        ListView alarmlist=(ListView)findViewById(R.id.alarmlist);
        adapter =new MyListAdapter(this, data, new MyListAdapter.SetAlarmListener() {
            @Override
            public void SetAlarm(final int postion) {

                AlarmSetDialog alarmSetDialog =new AlarmSetDialog(MainActivity.this, R.style.AlarmsetDialog, new AlarmSetDialog.SetRepeatListener() {
                    @Override
                    public void setrepeat(Map<String, Object> data) {
//                        int postion= (int) data.get("postion");
                        SharedPreferences sp = getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE);
                        String getcontent = sp.getString(String.valueOf(postion) + "repeat", null);
                        if (getcontent == null) {
                            for (int i = 0; i < 7; i++) {

                            }
                        } else {

                        }

                        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE).edit();
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < data.size(); i++) {
                            sb.append(data.get("day1")).append(",").append(data.get("day2")).append(",").append(data.get("day3")).append(",").append(data.get("day4")).append(",")
                                    .append(data.get("day5")).append(",").append(data.get("day6")).append(",").append(data.get("day7")).append(",");

                        }
                        if (sb.length() > 1) {
                            String content = sb.toString().substring(0, sb.length() - 1);
                            editor.putString(String.valueOf(postion) + "repeat", content);

                        } else {
                            editor.putString("alarmlist", null);
                        }
                        editor.commit();


                    }
                }, new AlarmSetDialog.SetDeleteListener() {
                    @Override
                    public void delete(int postion) {
                        int id = Integer.valueOf((String) data.get(postion).get("id"));
                        cancelalarm(id);
                        data.remove(postion);
                        adapter.refresh(data);
                        savealarm();
                    }
                }, new AlarmSetDialog.EveryDayListener() {
                    @Override
                    public void everyday(int postion) {
                        int id = Integer.valueOf((String) data.get(postion).get("id"));
                        String time= (String) data.get(postion).get("title");
                        String [] content =time.split(":");
                        int hour=Integer.valueOf(content[0]);
                        int minnute=Integer.valueOf(content[1]);
                        Calendar calendar1=Calendar.getInstance();
                        calendar1.set(Calendar.HOUR_OF_DAY, hour);
                        calendar1.set(Calendar.MINUTE, minnute);
                        calendar1.set(Calendar.SECOND, 0);
                        calendar1.set(Calendar.MILLISECOND, 0);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar1.getTimeInMillis()+AlarmManager.INTERVAL_DAY,
                        AlarmManager.INTERVAL_DAY,
                        PendingIntent.getBroadcast(MainActivity.this, id, new Intent(MainActivity.this, AlarmReceiver.class), 0));
                    }
                },postion);
                alarmSetDialog.show();




            }
        }, new MyListAdapter.CancelAlarmListener() {
            @Override
            public void CancelAlarm(int postion) {
                Map<String,Object>  map=new HashMap<String,Object>();

                int id = Integer.valueOf((String) data.get(postion).get("id"));
                String time= (String) data.get(postion).get("title");
                map.put("title",time);
                map.put("check",false);
                map.put("id",id);
                data.set(postion, map);
                cancelalarm(id);
                savealarm();

            }
        }, new MyListAdapter.ChangeTimeListener() {
            @Override
            public void ChangeTime(int postion) {
                int id = Integer.valueOf((String) data.get(postion).get("id"));
                cancelalarm(id);
                changealarm(postion);
            }
        });
        alarmlist.setAdapter(adapter);
        toolbar.setTitle("shakeAlarm");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatalarm();
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }
    private void cancelalarm(int id){
        alarmManager.cancel(PendingIntent.getBroadcast(MainActivity.this, id, new Intent(MainActivity.this, AlarmReceiver.class), 0));
    }

    private  void changealarm(final int postion){
        final Calendar calendar=Calendar.getInstance();
        new TimePickerDialog( this, new TimePickerDialog.OnTimeSetListener() {
            int i=0;
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar1.set(Calendar.MINUTE, minute);
                calendar1.set(Calendar.SECOND, 0);
                calendar1.set(Calendar.MILLISECOND, 0);

                /*判断是否需要移到下一天*/
                Calendar calendar2 = Calendar.getInstance();

                if (calendar1.getTimeInMillis() <= calendar2.getTimeInMillis()) {
                    calendar1.setTimeInMillis(calendar1.getTimeInMillis() + 24 * 60 * 60 * 1000);
                }
                int id = (int) calendar1.getTimeInMillis() / 60 / 1000;
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        calendar1.getTimeInMillis(), PendingIntent.getBroadcast(MainActivity.this, id, new Intent(MainActivity.this, AlarmReceiver.class), 0));

//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                        calendar1.getTimeInMillis(),
//                        5 * 60 * 1000,
//                        PendingIntent.getBroadcast(MainActivity.this, 4512, new Intent(MainActivity.this, AlarmReceiver.class), 0));
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(calendar1.getTimeInMillis());
                String hour="";

                if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
                    hour = "0" + String.valueOf(date.get(Calendar.HOUR_OF_DAY));
                }
                else {
                    hour= String.valueOf(date.get(Calendar.HOUR_OF_DAY));
                }
                String minute2="";
                if (date.get(Calendar.MINUTE) < 10) {
                    minute2 = "0" + String.valueOf(date.get(Calendar.MINUTE));
                }
                else {
                    minute2= String.valueOf(date.get(Calendar.MINUTE));
                }
                String timeLabel = hour+":"+minute2;

                Map<String, Object> map = new HashMap<String, Object>();
                i++;
                if (i == 1) {
                    String id2=String.valueOf(id);

                    map.put("title", timeLabel);
                    map.put("check", true);
                    map.put("id", id2);

                    data.set(postion,map);
                    adapter.refresh(data);
                    savealarm();
                }

            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }

    private void creatalarm(){
        final Calendar calendar=Calendar.getInstance();
        new TimePickerDialog( this, new TimePickerDialog.OnTimeSetListener() {
            int i=0;
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar1.set(Calendar.MINUTE, minute);
                calendar1.set(Calendar.SECOND, 0);
                calendar1.set(Calendar.MILLISECOND, 0);

                /*判断是否需要移到下一天*/
                Calendar calendar2 = Calendar.getInstance();

                if (calendar1.getTimeInMillis() <= calendar2.getTimeInMillis()) {
                    calendar1.setTimeInMillis(calendar1.getTimeInMillis() + 24 * 60 * 60 * 1000);
                }
                int id = (int) calendar1.getTimeInMillis() / 60 / 1000;
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        calendar1.getTimeInMillis(), PendingIntent.getBroadcast(MainActivity.this, id, new Intent(MainActivity.this, AlarmReceiver.class), 0));

//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                        calendar1.getTimeInMillis(),
//                        5 * 60 * 1000,
//                        PendingIntent.getBroadcast(MainActivity.this, 4512, new Intent(MainActivity.this, AlarmReceiver.class), 0));
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(calendar1.getTimeInMillis());
                String hour="";

                if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
                    hour = "0" + String.valueOf(date.get(Calendar.HOUR_OF_DAY));
                }
                else {
                    hour= String.valueOf(date.get(Calendar.HOUR_OF_DAY));
                }
              String minute2="";
                if (date.get(Calendar.MINUTE) < 10) {
                    minute2 = "0" + String.valueOf(date.get(Calendar.MINUTE));
                }
                else {
                    minute2= String.valueOf(date.get(Calendar.MINUTE));
                }
                String timeLabel = hour+":"+minute2;

                Map<String, Object> map = new HashMap<String, Object>();
                i++;
                if (i == 1) {
                    String id2=String.valueOf(id);

                    map.put("title", timeLabel);
                    map.put("check", true);
                    map.put("id", id2);

                    data.add(map);
                    adapter.refresh(data);
                    savealarm();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "需摇一摇来解除闹钟", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();

    }

    public void savealarm(){

//        MyListAdapter adapter =new MyListAdapter(this,data);
//        ListView alarmlist=(ListView)findViewById(R.id.alarmlist);
//
//        alarmlist.setAdapter(adapter);
       // adapter.notifyDataSetChanged();
        SharedPreferences.Editor editor =this.getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE).edit();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<data.size();i++){
            sb.append(data.get(i).get("title")).append(",").append(data.get(i).get("check")).append(",").append(data.get(i).get("id")).append(",");

        }
        if (sb.length()>1) {
            String content = sb.toString().substring(0, sb.length()-1);
            editor.putString("alarmlist", content);

        }else{
            editor.putString("alarmlist", null);
        }
        editor.commit();
    }
    public List<Map<String, Object>> readdata(){
        List<Map<String, Object>> data2=new ArrayList<Map<String, Object>>() ;
        SharedPreferences sp=this.getSharedPreferences(MainActivity.class.getName(),Context.MODE_PRIVATE);
        String content =sp.getString("alarmlist", null);
        System.out.println(content+"222222222222");
        if(content!=null){
            String[] contentstring=content.split(",");
//            for(int i=0;i<contentstring.length;i++){
//                Log.d("shijian",contentstring[i]);
//            }
            for(int i=0;i<contentstring.length;i=i+3){
                Map<String, Object> map=new HashMap<String, Object>();
            map.put("title",contentstring[i]);
            map.put("check",Boolean.valueOf(contentstring[i+1]));
            map.put("id",contentstring[i+2]);
            data2.add(map);
        }
        }
        return data2;
    }
}
