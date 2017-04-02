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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.justaudio.services.NetworkUtils;
import com.justaudio.utils.UILoader;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by VIDYA
 */
public class NotificationPlayer implements PlayerService.JcPlayerServiceListener {

    private static final int NOTIFICATION_ID = 100;
    private static final int NEXT_ID = 0;
    private static final int PREVIOUS_ID = 1;
    private static final int PLAY_ID = 2;
    private static final int PAUSE_ID = 3;
    private static final int CLOSE_ID = 4;
    static final String NEXT = "NEXT";
    static final String CLOSE = "CLOSE";
    static final String PREVIOUS = "PREVIOUS";
    static final String PAUSE = "PAUSE";
    static final String PLAY = "PLAY";
    static final String ACTION = "ACTION";
    static final String PLAYLIST = "PLAYLIST";
    static final String CURRENT_AUDIO = "CURRENT_AUDIO";

    private NotificationManager notificationManager;
    private HomeActivity context;

    private String title;
    private String time = "00:00";
    private String iconResource;
    private String imagePath;

    NotificationPlayer(HomeActivity context) {
        this.context = context;
    }

    void createNotificationPlayer(final String title, String iconResourceResource) {

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
                    .setSmallIcon(R.drawable.icon_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.icon_notification))
                    .setCustomContentView(contentView)
                    .setContentIntent(PendingIntent.getActivity(context, NOTIFICATION_ID,
                            openUi, PendingIntent.FLAG_CANCEL_CURRENT));

            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notificationManager.notify(NOTIFICATION_ID, notification);

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.icon_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.icon_notification))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setCustomContentView(contentView)
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
        try {
            Drawable d = context.iv_player_image.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            remoteView.setImageViewBitmap(R.id.icon_player, bitmap);
        } catch (NullPointerException e) {
            remoteView.setImageViewResource(R.id.icon_player, R.drawable.icon_list_holder);
        }


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

    @Override
    public void updateImage(final String image) {
        context.iv_player_image.post(new Runnable() {
            @Override
            public void run() {
                UILoader.UILPicLoading(context.iv_player_image, image, null,
                        R.drawable.icon_list_holder);
            }
        });
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