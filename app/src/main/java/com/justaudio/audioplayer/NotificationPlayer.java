package com.justaudio.audioplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.justaudio.utils.UILoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by VIDYA
 */
public class NotificationPlayer implements PlayerService.JcPlayerServiceListener {

    public static final int NOTIFICATION_ID = 100;
    public static final int NEXT_ID = 0;
    public static final int PREVIOUS_ID = 1;
    public static final int PLAY_ID = 2;
    public static final int PAUSE_ID = 3;
    public static final int CLOSE_ID = 4;
    public static final String NEXT = "NEXT";
    public static final String CLOSE = "CLOSE";
    public static final String PREVIOUS = "PREVIOUS";
    public static final String PAUSE = "PAUSE";
    public static final String PLAY = "PLAY";
    public static final String ACTION = "ACTION";
    public static final String PLAYLIST = "PLAYLIST";
    public static final String CURRENT_AUDIO = "CURRENT_AUDIO";

    private NotificationManager notificationManager;
    private HomeActivity context;

    private String title;
    private String time = "00:00";
    private String iconResource;

    public NotificationPlayer(HomeActivity context) {
        this.context = context;
    }

    public void createNotificationPlayer(final String title, String iconResourceResource) {

        AudioPlayer.getInstance().registerNotificationListener(this);


        this.title = title;
        this.iconResource = iconResourceResource;
        final Intent openUi = new Intent(context, context.getClass());
        openUi.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


        if (notificationManager == null)
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        ServiceConnection mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Intent intent = new Intent(context, KillNotificationsService.class);
                ((KillNotificationsService.KillBinder) service).service.startService(intent);
                if (AudioPlayer.getInstance() != null)
                    createNotificationData(openUi);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        context.bindService(new Intent(context, KillNotificationsService.class),
                mConnection, Context.BIND_AUTO_CREATE);
    }


    private void createNotificationData(Intent openUi) {

        RemoteViews contentView = createBigNotificationView();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setCategory(Notification.CATEGORY_SOCIAL)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_notification_default_white)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_notification_default_white))
                    .setContent(contentView)
                    .setContentIntent(PendingIntent.getActivity(context, NOTIFICATION_ID,
                            openUi, PendingIntent.FLAG_CANCEL_CURRENT));

            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notificationManager.notify(NOTIFICATION_ID, notification);

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notification_default_white)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_notification_default_white))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContent(contentView)
                    .setContentIntent(PendingIntent.getActivity(context, NOTIFICATION_ID, openUi,
                            PendingIntent.FLAG_CANCEL_CURRENT));

            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }


    void updateNotification() {
        createNotificationPlayer(title, iconResource);
    }


    private RemoteViews createBigNotificationView() {
        RemoteViews remoteView;
        if (AudioPlayer.getInstance().isPaused()) {
            remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_play);
            remoteView.setOnClickPendingIntent(R.id.btn_play_notification, buildPendingIntent(PLAY, PLAY_ID));
            remoteView.setOnClickPendingIntent(R.id.iv_notification_cancel, buildPendingIntent(CLOSE, CLOSE_ID));
        } else {
            remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_pause);
            remoteView.setOnClickPendingIntent(R.id.btn_pause_notification, buildPendingIntent(PAUSE, PAUSE_ID));
            remoteView.setOnClickPendingIntent(R.id.iv_notification_cancel, buildPendingIntent(CLOSE, CLOSE_ID));
        }


        remoteView.setTextViewText(R.id.txt_current_music_notification, title);
        remoteView.setTextViewText(R.id.txt_duration_notification, time);
        remoteView.setImageViewResource(R.id.icon_player, R.mipmap.ic_launcher);
        //UILoader.UILNotificationPicLoading(remoteView, R.id.icon_player, iconResource);
        remoteView.setOnClickPendingIntent(R.id.btn_next_notification, buildPendingIntent(NEXT, NEXT_ID));
        remoteView.setOnClickPendingIntent(R.id.btn_prev_notification, buildPendingIntent(PREVIOUS, PREVIOUS_ID));

        return remoteView;
    }


    private PendingIntent buildPendingIntent(String action, int id) {
        Intent playIntent = new Intent(context.getApplicationContext(), PlayerNotificationReceiver.class);
        playIntent.putExtra(ACTION, action);

        return PendingIntent.getBroadcast(context.getApplicationContext(),
                id, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onPreparedAudio(String audioName, int duration) {

    }

    @Override
    public void onCompletedAudio() {

    }

    @Override
    public void onPaused() {
        createNotificationPlayer(title, iconResource);
    }

    @Override
    public void onContinueAudio() {

    }

    @Override
    public void onPlaying() {
        createNotificationPlayer(title, iconResource);
    }

    @Override
    public void onTimeChanged(long currentTime) {
        long aux = currentTime / 1000;
        int minutes = (int) (aux / 60);
        int seconds = (int) (aux % 60);
        final String sMinutes = minutes < 10 ? "0" + minutes : minutes + "";
        final String sSeconds = seconds < 10 ? "0" + seconds : seconds + "";
        this.time = sMinutes + ":" + sSeconds;

        createNotificationPlayer(title, iconResource);
    }

    @Override
    public void updateTitle(String title) {
        this.title = title;
        createNotificationPlayer(title, iconResource);
    }

    public void destroyNotificationIfExists() {
        if (notificationManager != null)
            try {
                notificationManager.cancel(NOTIFICATION_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
    }
}