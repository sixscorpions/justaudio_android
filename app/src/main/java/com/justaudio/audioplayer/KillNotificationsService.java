package com.justaudio.audioplayer;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by ${VIDYA}
 */

public class KillNotificationsService extends Service {

    class KillBinder extends Binder {
        final Service service;

        KillBinder(Service service) {
            this.service = service;
        }

    }

    public static int NOTIFICATION_ID = 666;
    private final IBinder mBinder = new KillBinder(this);

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNM.cancel(NOTIFICATION_ID);
    }
}
