package com.lvyteam.shakealarm;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<Map<String, Object>> data;
    private AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        alarmManager=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        List<Map<String,Object>> data =getdata();
        ListView alarmlist=(ListView)findViewById(R.id.alarmlist);
        MyListAdapter adapter =new MyListAdapter(this,data);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void creatalarm(){
        final Calendar calendar=Calendar.getInstance();
        new TimePickerDialog( this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar1.set(Calendar.MINUTE,minute);
                calendar1.set(Calendar.SECOND,0);
                calendar1.set(Calendar.MILLISECOND, 0);

                /*判断是否需要移到下一天*/
                Calendar calendar2=Calendar.getInstance();

                if(calendar1.getTimeInMillis()<=calendar2.getTimeInMillis()){
                    calendar1.setTimeInMillis(calendar1.getTimeInMillis()+24*60*60*1000);
                }

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar1.getTimeInMillis(),
                        5*60*1000,
                        PendingIntent.getBroadcast(MainActivity.this, 4512, new Intent(MainActivity.this,AlarmReceiver.class), 0));

                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(calendar1.getTimeInMillis());
                String  timeLabel = String.format(" %d:%d",

                        date.get(Calendar.HOUR_OF_DAY),
                        date.get(Calendar.MINUTE));

                Map<String, Object> map=new HashMap<String, Object>();


                map.put("title",timeLabel);
                map.put("check", true);


                savealarm(map);
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();

    }
    private void savealarm(Map<String,Object> map){
        List<Map<String,Object>> data=readdata();
        data.add(map);
        MyListAdapter adapter =new MyListAdapter(this,data);
        ListView alarmlist=(ListView)findViewById(R.id.alarmlist);
        alarmlist.setAdapter(adapter);
       // adapter.notifyDataSetChanged();

        SharedPreferences.Editor editor =this.getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE).edit();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<data.size();i++){
            sb.append(data.get(i).get("title")).append(",").append(data.get(i).get("check")).append(",");

        }
        if (sb.length()>1) {
            String content = sb.toString().substring(0, sb.length()-1);

            editor.putString("alarmlist", content);

            System.out.println(content);
        }else{
            editor.putString("alarmlist", null);
        }

        editor.commit();
    }
    public List<Map<String, Object>> readdata(){
        List<Map<String, Object>> data=new ArrayList<Map<String, Object>>() ;
        SharedPreferences sp=this.getSharedPreferences(MainActivity.class.getName(),Context.MODE_PRIVATE);
        String content =sp.getString("alarmlist", null);
        if(content!=null){
            String[] contentstring=content.split(",");
//            for(int i=0;i<contentstring.length;i++){
//                Log.d("shijian",contentstring[i]);
//            }


            for(int i=0;i<contentstring.length;i=i+2){
                Map<String, Object> map=new HashMap<String, Object>();

                   map.put("title",contentstring[i]);
                   map.put("check",Boolean.valueOf(contentstring[i+1]));



                data.add(map);
            }
        }



        return data;

    }


    public List<Map<String, Object>> getdata() {
        List<Map<String, Object>> data=new ArrayList<Map<String, Object>>() ;
        data=readdata();
//        for(int i=0;i<10;i++){
//            Map<String, Object> map=new HashMap<String, Object>();
//            map.put("title","10:03");
//            map.put("check",false);
//            data.add(map);
//        }


        return data;
    }
}
