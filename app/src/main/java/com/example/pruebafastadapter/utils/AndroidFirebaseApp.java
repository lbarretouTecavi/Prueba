package com.example.pruebafastadapter.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AndroidFirebaseApp extends Application {
    public static final String FCM_CHANNEL_ID = "FCM_CHANNEL_ID";

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel fcmChannel = new NotificationChannel(FCM_CHANNEL_ID, "FCM_Channel", NotificationManager.IMPORTANCE_UNSPECIFIED);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(fcmChannel);
        }
    }
}
