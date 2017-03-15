package com.justaudio.audioplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.fragment.PlayerListFragment;
import com.justaudio.utils.AudioUtils;

import java.util.ArrayList;
import java.util.List;

public class AudioPlayerView extends LinearLayout implements
        PlayerService.JcPlayerServiceListener,
        View.OnClickListener, SeekBar.OnSeekBarChangeListener, PlayerService.OnInvalidPathListener {

    private static final int PULSE_ANIMATION_DURATION = 200;
    private static final int TITLE_ANIMATION_DURATION = 600;

    private TextView txtCurrentMusic;
    public ImageButton btnPrev;
    public ImageButton btnPlay;
    public ImageButton btnNext;
    public SeekBar seekBar;

    private ProgressBar progressBarPlayer;
    public AudioPlayer audioPlayer;
    private TextView txtDuration;

    private TextView txtCurrentDuration;
    private AssetFileDescriptor assetFileDescriptor;
    private boolean initialized;
    private HomeActivity parent;

    public static PlayerListFragment fragment;

    public AudioPlayerView(Context context) {
        super(context);
        init();
    }

    public AudioPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AudioPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setInitializeActivity(HomeActivity activity) {
        this.parent = activity;
    }

    private void init() {
        inflate(getContext(), R.layout.layout_audio_player_controller_new, this);

        this.progressBarPlayer = (ProgressBar) findViewById(R.id.progress_bar_player);
        this.btnNext = (ImageButton) findViewById(R.id.btn_next);
        this.btnPrev = (ImageButton) findViewById(R.id.btn_prev);
        this.btnPlay = (ImageButton) findViewById(R.id.btn_play);
        this.txtDuration = (TextView) findViewById(R.id.txt_total_duration);
        this.txtCurrentDuration = (TextView) findViewById(R.id.txt_current_duration);
        this.txtCurrentMusic = (TextView) findViewById(R.id.txt_current_music);
        this.seekBar = (SeekBar) findViewById(R.id.seek_bar);
        this.btnPlay.setTag(R.drawable.icon_player_play);

        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    /**
     * Initialize the playlist and controls.
     *
     * @param playlist List of TrackAudioModel objects that you want play
     */
    public void initPlaylist(List<TrackAudioModel> playlist) {

        sortPlaylist(playlist);

        audioPlayer = new AudioPlayer(getContext(), playlist, AudioPlayerView.this);
        audioPlayer.registerInvalidPathListener(this);
        initialized = true;
    }

    /**
     * Initialize an anonymous playlist with a default JcPlayer title for all audios
     *
     * @param playlist List of urls strings
     */
    public void initAnonPlaylist(List<TrackAudioModel> playlist) {
        sortPlaylist(playlist);
        generateTitleAudio(playlist, getContext().getString(R.string.track_number));
        audioPlayer = new AudioPlayer(getContext(), playlist, AudioPlayerView.this);
        audioPlayer.registerInvalidPathListener(this);
        initialized = true;
    }

    /**
     * Initialize an anonymous playlist, but with a custom title for all audios
     *
     * @param playlist List of TrackAudioModel files.
     * @param title    Default title for all audios
     */
    public void initWithTitlePlaylist(List<TrackAudioModel> playlist, String title) {
        sortPlaylist(playlist);
        generateTitleAudio(playlist, title);
        audioPlayer = new AudioPlayer(getContext(), playlist, AudioPlayerView.this);
        audioPlayer.registerInvalidPathListener(this);
        initialized = true;
    }

    /**
     * Add an audio for the playlist
     */
    //TODO: Should we expose this to user? A: Yes, because the user can add files to playlist without creating a new List of TrackAudioModel objects, just adding this files dynamically.
    public void addAudio(TrackAudioModel trackAudioModel) {
        createJcAudioPlayer();
        List<TrackAudioModel> playlist = audioPlayer.getPlaylist();
        int lastPosition = playlist.size();

        trackAudioModel.setId(lastPosition + 1);
        trackAudioModel.setPosition(lastPosition + 1);

        if (!playlist.contains(trackAudioModel))
            playlist.add(lastPosition, trackAudioModel);
    }

    /**
     * Remove an audio for the playlist
     *
     * @param trackAudioModel TrackAudioModel object
     */
    public void removeAudio(TrackAudioModel trackAudioModel) {
        if (audioPlayer != null) {
            List<TrackAudioModel> playlist = audioPlayer.getPlaylist();

            if (playlist != null && playlist.contains(trackAudioModel))
                playlist.remove(trackAudioModel);
        }
    }

    public void playAudio(TrackAudioModel trackAudioModel, Fragment fragment) {
        AudioPlayerView.fragment = (PlayerListFragment) fragment;
        showProgressBar();
        seekBar.setProgress(0);
        createJcAudioPlayer();
        if (!audioPlayer.getPlaylist().contains(trackAudioModel))
            audioPlayer.getPlaylist().add(trackAudioModel);

        try {
            audioPlayer.playAudio(trackAudioModel);
            AudioUtils.showPlayerControl(parent);
        } catch (AudioListNullPointerException e) {
            dismissProgressBar();
            e.printStackTrace();
        }
    }

    public void next() {
        if (fragment != null) {
            resetPlayerInfo();
            showProgressBar();
            try {
                audioPlayer.nextAudio();
            } catch (AudioListNullPointerException e) {
                dismissProgressBar();
                e.printStackTrace();
            }
            fragment.updateUI();
        }
    }

    public void continueAudio() {
        showProgressBar();

        try {
            audioPlayer.continueAudio();
        } catch (AudioListNullPointerException e) {
            dismissProgressBar();
            e.printStackTrace();
        }
    }

    public void pause() {
        audioPlayer.pauseAudio();
    }

    public void previous() {
        if (fragment != null) {
            resetPlayerInfo();
            showProgressBar();

            try {
                audioPlayer.previousAudio();
            } catch (AudioListNullPointerException e) {
                dismissProgressBar();
                e.printStackTrace();
            }
            fragment.updatePreUI();
        }
    }

    @Override
    public void onClick(View view) {
        if (initialized)
            if (view.getId() == R.id.btn_play) {
                YoYo.with(Techniques.Pulse)
                        .duration(PULSE_ANIMATION_DURATION)
                        .playOn(btnPlay);

                if (btnPlay.getTag().equals(R.drawable.icon_player_pause))
                    pause();
                else
                    continueAudio();
            }

        if (view.getId() == R.id.btn_next) {
            YoYo.with(Techniques.Pulse)
                    .duration(PULSE_ANIMATION_DURATION)
                    .playOn(btnNext);
            next();
        }

        if (view.getId() == R.id.btn_prev) {
            YoYo.with(Techniques.Pulse)
                    .duration(PULSE_ANIMATION_DURATION)
                    .playOn(btnPrev);
            previous();
        }
    }

    /**
     * Create a notification player with same playlist with a custom icon.
     *
     * @param iconResource icon path.
     */
    public void createNotification(int iconResource) {
        if (audioPlayer != null) audioPlayer.createNewNotification(iconResource);
    }

    /**
     * Create a notification player with same playlist with a default icon
     */
    public void createNotification() {
        if (audioPlayer != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // For light theme
                audioPlayer.createNewNotification(R.drawable.ic_notification_default_black);
            } else {
                // For dark theme
                audioPlayer.createNewNotification(R.drawable.ic_notification_default_white);
            }
        }
    }

    public List<TrackAudioModel> getMyPlaylist() {
        return audioPlayer.getPlaylist();
    }

    public TrackAudioModel getCurrentAudio() {
        return audioPlayer.getCurrentAudio();
    }

    private void createJcAudioPlayer() {
        if (audioPlayer == null) {
            List<TrackAudioModel> playlist = new ArrayList<>();
            audioPlayer = new AudioPlayer(getContext(), playlist, AudioPlayerView.this);
        }
        audioPlayer.registerInvalidPathListener(this);
        initialized = true;
    }

    private void sortPlaylist(List<TrackAudioModel> playlist) {
        for (int i = 0; i < playlist.size(); i++) {
            TrackAudioModel trackAudioModel = playlist.get(i);
            trackAudioModel.setPosition(i);
        }
    }

    private void generateTitleAudio(List<TrackAudioModel> playlist, String title) {
        for (int i = 0; i < playlist.size(); i++) {
            if (title.equals(getContext().getString(R.string.txt_cd)))
                playlist.get(i).setTitle(getContext().getString(R.string.txt_cancel) + " " + String.valueOf(i + 1));
            else
                playlist.get(i).setTitle(title);
        }
    }

    private void showProgressBar() {
        progressBarPlayer.setVisibility(ProgressBar.VISIBLE);
        btnPlay.setVisibility(Button.GONE);
        btnNext.setClickable(false);
        btnPrev.setClickable(false);
    }

    private void dismissProgressBar() {
        progressBarPlayer.setVisibility(ProgressBar.GONE);
        btnPlay.setVisibility(Button.VISIBLE);
        btnNext.setClickable(true);
        btnPrev.setClickable(true);
    }

    @Override
    public void onPreparedAudio(String audioName, int duration) {
        dismissProgressBar();
        resetPlayerInfo();

        long aux = duration / 1000;
        int minute = (int) (aux / 60);
        int second = (int) (aux % 60);

        final String sDuration =
                // Minutes
                (minute < 10 ? "0" + minute : minute + "")
                        + ":" +
                        // Seconds
                        (second < 10 ? "0" + second : second + "");

        seekBar.setMax(duration);

        txtDuration.post(new Runnable() {
            @Override
            public void run() {
                txtDuration.setText(sDuration);
            }
        });
    }

    @Override
    public void onCompletedAudio() {
        resetPlayerInfo();
        try {
            audioPlayer.nextAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPlayerInfo() {
        seekBar.setProgress(0);
        txtCurrentMusic.setText("");
        txtCurrentDuration.setText(getContext().getString(R.string.play_initial_time));
        txtDuration.setText(getContext().getString(R.string.play_initial_time));
    }

    @Override
    public void onPaused() {
        btnPlay.setBackground(ResourcesCompat.getDrawable(getResources(),
                R.drawable.icon_player_play, null));
        btnPlay.setTag(R.drawable.icon_player_play);
    }

    @Override
    public void onContinueAudio() {
        dismissProgressBar();
    }

    @Override
    public void onPlaying() {
        btnPlay.setBackground(ResourcesCompat.getDrawable(getResources(),
                R.drawable.icon_player_pause, null));
        btnPlay.setTag(R.drawable.icon_player_pause);
    }

    @Override
    public void onTimeChanged(long currentPosition) {
        long aux = currentPosition / 1000;
        int minutes = (int) (aux / 60);
        int seconds = (int) (aux % 60);
        final String sMinutes = minutes < 10 ? "0" + minutes : minutes + "";
        final String sSeconds = seconds < 10 ? "0" + seconds : seconds + "";

        seekBar.setProgress((int) currentPosition);
        txtCurrentDuration.post(new Runnable() {
            @Override
            public void run() {
                txtCurrentDuration.setText(String.valueOf(sMinutes + ":" + sSeconds));
            }
        });
    }

    @Override
    public void updateTitle(String title) {
        final String mTitle = title;

        YoYo.with(Techniques.FadeInLeft)
                .duration(TITLE_ANIMATION_DURATION)
                .playOn(txtCurrentMusic);

        txtCurrentMusic.post(new Runnable() {
            @Override
            public void run() {
                txtCurrentMusic.setText(mTitle);
            }
        });
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
        if (fromUser) audioPlayer.seekTo(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        showProgressBar();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        dismissProgressBar();
    }

    public void registerInvalidPathListener(PlayerService.OnInvalidPathListener registerInvalidPathListener) {
        if (audioPlayer != null)
            audioPlayer.registerInvalidPathListener(registerInvalidPathListener);
    }

    public void kill() {
        if (audioPlayer != null) audioPlayer.kill();
    }

    @Override
    public void onPathError(TrackAudioModel trackAudioModel) {
        dismissProgressBar();
    }

    public void registerServiceListener(PlayerService.JcPlayerServiceListener jcPlayerServiceListener) {
        if (audioPlayer != null)
            audioPlayer.registerServiceListener(jcPlayerServiceListener);
    }
}
