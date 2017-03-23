package com.justaudio.audioplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.justaudio.activities.HomeActivity;

public class PlayerNotificationReceiver extends BroadcastReceiver {
    public PlayerNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AudioPlayer audioPlayer = AudioPlayer.getInstance();
        String action = "";

        if (intent.hasExtra(NotificationPlayer.ACTION))
            action = intent.getStringExtra(NotificationPlayer.ACTION);

        switch (action) {
            case NotificationPlayer.PLAY:
                try {
                    audioPlayer.continueAudio();
                    audioPlayer.updateNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case NotificationPlayer.PAUSE:
                audioPlayer.pauseAudio();
                audioPlayer.updateNotification();
                break;

            case NotificationPlayer.NEXT:
                try {
                    audioPlayer.nextAudio();
                } catch (AudioListNullPointerException e) {
                    try {
                        audioPlayer.continueAudio();
                    } catch (AudioListNullPointerException e1) {
                        e1.printStackTrace();
                    }
                }

                if (HomeActivity.pause_button_position == audioPlayer.getPlaylist().size() - 1)
                    HomeActivity.pause_button_position = 0;
                else
                    HomeActivity.pause_button_position = HomeActivity.pause_button_position + 1;

                if (HomeActivity.adapter != null)
                    HomeActivity.adapter.notifyDataSetChanged();

                break;

            case NotificationPlayer.PREVIOUS:
                try {
                    audioPlayer.previousAudio();
                } catch (Exception e) {
                    try {
                        audioPlayer.continueAudio();
                    } catch (AudioListNullPointerException e1) {
                        e1.printStackTrace();
                    }
                }

                if (HomeActivity.pause_button_position == 0)
                    HomeActivity.pause_button_position = audioPlayer.getPlaylist().size() - 1;
                else
                    HomeActivity.pause_button_position = HomeActivity.pause_button_position - 1;

                if (HomeActivity.adapter != null)
                    HomeActivity.adapter.notifyDataSetChanged();
                break;
        }
    }
}
