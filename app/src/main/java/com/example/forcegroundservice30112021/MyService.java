package com.example.forcegroundservice30112021;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    String CHANNEL_ID = "My Channel";

    NotificationManager mNotificationManager;
    Notification mNotification;

    // chỉ sử dụng khi dùng bound service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BBB","onCreate");
        mNotification = createNotification();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(1,mNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BBB","onStartCommand");

        if (intent != null){
            String text = intent.getStringExtra("text");
            Log.d("BBB","Data receive " + text);
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BBB","onDestroy");
    }

    private Notification createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, CHANNEL_ID);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setSmallIcon(android.R.drawable.ic_menu_add);
        builder.setShowWhen(true);
        builder.setContentTitle("Ứng dụng có phiên bản mới");
        builder.setContentText("Bạn muốn cập nhật hay không?");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }
}
