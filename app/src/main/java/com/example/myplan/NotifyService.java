package com.example.myplan;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class NotifyService extends Service {
    public NotifyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void createNotification(){
        Intent intent = new Intent(this,CalenderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        Notification.Builder notification = new Notification.Builder(this)
                .setContentTitle("Event Alert")
                .setContentText("ABCD")
                .setSmallIcon(R.drawable.ic_baseline_calendar_today_24)
                .setAutoCancel(true);
    }
}
