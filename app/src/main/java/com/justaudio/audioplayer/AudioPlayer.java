package com.justaudio.audioplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.justaudio.dto.TrackAudioModel;
import com.justaudio.utils.AudioUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by VIDYA
 */

class AudioPlayer {
    private PlayerService playerService;
    private PlayerService.JcPlayerServiceListener listener;
    private PlayerService.OnInvalidPathListener invalidPathListener;
    private NotificationPlayer notificationPlayer;
    private List<TrackAudioModel> playlist;
    private TrackAudioModel currentTrackAudioModel;
    private int currentPositionList;
    private Context context;
    private static AudioPlayer instance = null;
    private boolean mBound = false;
    private boolean playing;
    private boolean paused;
    private int position = 1;

    AudioPlayer(Context context, List<TrackAudioModel> list, PlayerService.JcPlayerServiceListener listener) {
        this.context = context;
        this.playlist = list;
        this.listener = listener;
        instance = AudioPlayer.this;
        this.notificationPlayer = new NotificationPlayer(context);

        initService();
    }

    public void setInstance(AudioPlayer instance) {
        this.instance = instance;
    }

    public void registerNotificationListener(PlayerService.JcPlayerServiceListener notificationListener) {
        this.listener = notificationListener;
        if (notificationPlayer != null)
            playerService.registerNotificationListener(notificationListener);
    }

    public void registerInvalidPathListener(PlayerService.OnInvalidPathListener registerInvalidPathListener) {
        this.invalidPathListener = registerInvalidPathListener;
        if (playerService != null)
            playerService.registerInvalidPathListener(invalidPathListener);
    }

    public void registerServiceListener(PlayerService.JcPlayerServiceListener jcPlayerServiceListener) {
        this.listener = jcPlayerServiceListener;
        if (playerService != null)
            playerService.registerServicePlayerListener(jcPlayerServiceListener);
    }

    public static AudioPlayer getInstance() {
        return instance;
    }

    public void playAudio(TrackAudioModel TrackAudioModel) throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0)
            throw new AudioListNullPointerException();

        currentTrackAudioModel = TrackAudioModel;
        playerService.play(currentTrackAudioModel);
        updatePositionAudioList();
        playing = true;
        paused = false;
    }


    private void initService() {
        if (!mBound)
            startJcPlayerService();
        else
            mBound = true;
    }

    public void nextAudio() throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0)
            throw new AudioListNullPointerException();

        else {
            if (currentTrackAudioModel != null) {
                try {
                    TrackAudioModel nextTrackAudioModel = playlist.get(currentPositionList + position);
                    this.currentTrackAudioModel = nextTrackAudioModel;
                    playerService.stop();
                    playerService.play(nextTrackAudioModel);

                } catch (IndexOutOfBoundsException e) {
                    playAudio(playlist.get(0));
                    e.printStackTrace();
                }
            }
            updatePositionAudioList();
            playing = true;
            paused = false;
        }
    }

    public void previousAudio() throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0)
            throw new AudioListNullPointerException();

        else {
            if (currentTrackAudioModel != null) {
                try {
                    TrackAudioModel previousTrackAudioModel = playlist.get(currentPositionList - position);
                    this.currentTrackAudioModel = previousTrackAudioModel;
                    playerService.stop();
                    playerService.play(previousTrackAudioModel);

                } catch (IndexOutOfBoundsException e) {
                    playAudio(playlist.get(0));
                    e.printStackTrace();
                }
            }

            updatePositionAudioList();
            playing = true;
            paused = false;
        }
    }

    public void pauseAudio() {
        playerService.pause(currentTrackAudioModel);
        paused = true;
        playing = false;
    }

    public void continueAudio() throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0)
            throw new AudioListNullPointerException();

        else {
            if (currentTrackAudioModel == null)
                currentTrackAudioModel = playlist.get(0);
            playAudio(currentTrackAudioModel);
            playing = true;
            paused = false;
        }
    }

    public void createNewNotification(int iconResource) {
        if (currentTrackAudioModel != null)
            notificationPlayer.createNotificationPlayer(currentTrackAudioModel.getTitle(), iconResource);
    }

    public void updateNotification() {
        notificationPlayer.updateNotification();
    }

    public void seekTo(int time) {
        if (playerService != null)
            playerService.seekTo(time);
    }


    private void updatePositionAudioList() {
        for (int i = 0; i < playlist.size(); i++) {
            if (playlist.get(i).getId() == currentTrackAudioModel.getId())
                this.currentPositionList = i;
        }
    }

    private synchronized void startJcPlayerService() {
        if (!mBound) {
            Intent intent = new Intent(context.getApplicationContext(), PlayerService.class);
            intent.putExtra(NotificationPlayer.PLAYLIST, (Serializable) playlist);
            intent.putExtra(NotificationPlayer.CURRENT_AUDIO, currentTrackAudioModel);
            context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            PlayerService.JcPlayerServiceBinder binder = (PlayerService.JcPlayerServiceBinder) service;
            playerService = binder.getService();

            if (listener != null)
                playerService.registerServicePlayerListener(listener);

            if (invalidPathListener != null)
                playerService.registerInvalidPathListener(invalidPathListener);

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
            playing = false;
            paused = true;
        }
    };

    public boolean isPaused() {
        return paused;
    }

    public void kill() {
        if (playerService != null) {
            playerService.stop();
            playerService.destroy();
        }

        if (mBound)
            try {
                context.unbindService(mConnection);
            } catch (IllegalArgumentException e) {
                //TODO: Add readable exception here
            }

        if (notificationPlayer != null) {
            notificationPlayer.destroyNotificationIfExists();
        }

        if (AudioPlayer.getInstance() != null)
            AudioPlayer.getInstance().setInstance(null);
    }

    public List<TrackAudioModel> getPlaylist() {
        return playlist;
    }

    public TrackAudioModel getCurrentAudio() {
        return playerService.getCurrentAudio();
    }
}
