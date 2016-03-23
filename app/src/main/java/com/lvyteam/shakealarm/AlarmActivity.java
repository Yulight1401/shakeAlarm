package com.lvyteam.shakealarm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.Keyboard;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Vibrator vibrator;

    private static final int SENSOR_SHAKE = 10;
    private Button btn_delay;
    private AlarmManager alarmManager;
    private  MediaPlayer mediaPlayer;
    private PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    KeyguardManager km;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        playmusic();
        shaking();
        lighting();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final Calendar calendar=Calendar.getInstance();
        alarmManager=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        TextView textView=(TextView)findViewById(R.id.alarmtime);
        String minute2="";
        if (calendar.get(Calendar.MINUTE) < 10) {
            minute2 = "0" + String.valueOf(calendar.get(Calendar.MINUTE));
        }
        else {
            minute2= String.valueOf(calendar.get(Calendar.MINUTE));
        }
        String timeLabel = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+minute2;
        textView.setText(timeLabel);
        btn_delay=(Button)findViewById(R.id.btn_delay);

        btn_delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis()+5 * 60 * 1000,
                        5 * 60 * 1000,
                        PendingIntent.getBroadcast(AlarmActivity.this, 4512, new Intent(AlarmActivity.this, AlarmReceiver.class), 0));
                ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).restartPackage(getPackageName());
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

            }
        });

    }

    protected  void lighting(){
        powerManager =(PowerManager)getSystemService(POWER_SERVICE);
        km=(KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        KeyguardManager.KeyguardLock kmlock=km.newKeyguardLock("unlock");
        kmlock.disableKeyguard();
        wakeLock=powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.SCREEN_DIM_WAKE_LOCK,"SimpleTimer");
    }
    protected void shaking(){
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long [] p={300,800,300,800};
        vibrator.vibrate(p,2);
    }
    protected  void playmusic(){

        mediaPlayer=MediaPlayer.create(this,R.raw .mp3);
        mediaPlayer.start();


    }
    protected void onDestroy() {
        wakeLock.release();
        vibrator.cancel();
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();

        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    protected void onStop() {
        super.onStop();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }  /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

                @Override
                public void onSensorChanged(SensorEvent event) {

                    float[] values = event.values;
                    float x = values[0]; // x轴方向的重力加速度，向右为正
                    float y = values[1]; // y轴方向的重力加速度，向前为正
                    float z = values[2]; // z轴方向的重力加速度，向上为正

                    int medumValue = 19;//
                    if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {

                        Message msg = new Message();
                        msg.what = SENSOR_SHAKE;
                        handler.sendMessage(msg);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };

    /**
     * 动作执行
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:

                    alarmManager.cancel(PendingIntent.getBroadcast(AlarmActivity.this, 4512, new Intent(AlarmActivity.this, AlarmReceiver.class), 0));
                    ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).restartPackage(getPackageName());
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                    break;
            }
        }
    };



}
