package com.example.forcegroundservice30112021;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Random;

public class MyService extends Service {

    String CHANNEL_ID = "My Channel";

    NotificationManager mNotificationManager;
    Notification mNotification;
    boolean isRunning = false;
    Random mRandom;
    int value = 0;
    Handler mHandler;
    boolean isStop = false;
    Thread thread;
    // chỉ sử dụng khi dùng bound service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BBB", "onCreate");
        mNotification = createNotification("Thông báo", "Bắt đầu tải xuống");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mRandom = new Random();
        mHandler = new Handler(Looper.getMainLooper());
        startForeground(1, mNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    isRunning = true;
                    while (value <= 100000000 && !isStop) {
                        value += randomProgress();
                        Log.d("BBB", value + "");
                    }
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!isStop){
                                mNotification = createProgressNotification("Đang tải về", "", value);
                                mNotificationManager.notify(1, mNotification);
                            }
                        }
                    }, 0);
                }
            });
            thread.start();

        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
        if (thread != null){
            thread = null;
        }
        Log.d("BBB", "onDestroy");
    }

    private Notification createNotification(String title, String contextText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, CHANNEL_ID);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setSmallIcon(android.R.drawable.ic_menu_add);
        builder.setShowWhen(true);
        builder.setContentTitle(title);
        builder.setContentText(contextText);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

    private Notification createProgressNotification(String title, String contextText, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, CHANNEL_ID);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setSmallIcon(android.R.drawable.ic_menu_add);
        builder.setShowWhen(true);
        builder.setContentTitle(title);
        builder.setContentText(contextText);
        builder.setProgress(100000000, progress, false);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

    private int randomProgress() {
        return mRandom.nextInt(10) + 1;
    }
}
