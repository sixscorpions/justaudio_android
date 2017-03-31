package com.justaudio.utils;

import android.view.View;

import com.justaudio.activities.HomeActivity;

/**
 * Created by ${VIDYA}
 */

public class AudioUtils {


    public static void showPlayerControl(final HomeActivity parent) {
        if (parent != null && parent.fl_player.getVisibility() == View.GONE) {
            parent.runOnUiThread(new Runnable() {
                public void run() {
                    parent.fl_player.setVisibility(View.VISIBLE);
                    parent.player.setVisibility(View.VISIBLE);
                    parent.ll_empty_player.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public static void hidePlayerControl(final HomeActivity parent) {
        if (parent != null) {
            parent.runOnUiThread(new Runnable() {
                public void run() {
                    parent.fl_player.setVisibility(View.GONE);
                    parent.player.setVisibility(View.INVISIBLE);
                    parent.ll_empty_player.setVisibility(View.VISIBLE);
                }
            });
        }
    }


}
