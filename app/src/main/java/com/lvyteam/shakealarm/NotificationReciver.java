package com.lvyteam.shakealarm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class NotificationReciver extends BroadcastReceiver {
    KillActivityListener killActivityListener;
    public NotificationReciver() {
    }
    public  interface  KillActivityListener{
        public  void killactivity();
    }
    public void setListener (KillActivityListener killActivityListener){
        this.killActivityListener=killActivityListener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
       AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        final Calendar calendar=Calendar.getInstance();
        alarmManager.cancel(PendingIntent.getBroadcast(context, 4512, new Intent(context, AlarmReceiver.class), 0));
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis() + 5 * 60 * 1000,
//                5 * 60 * 1000,
//                PendingIntent.getBroadcast(context, 4512, new Intent(context, AlarmReceiver.class), 0));
              //  killActivityListener.killactivity();


    }
}
