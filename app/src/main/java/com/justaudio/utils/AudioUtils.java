package com.justaudio.utils;

import android.view.View;

import com.justaudio.activities.HomeActivity;

/**
 * Created by ${VIDYA}
 */

public class AudioUtils {


    public static void showPlayerControl(HomeActivity parent) {
        if (parent != null) {
            parent.player.setVisibility(View.VISIBLE);
            parent.ll_empty_player.setVisibility(View.GONE);
        }
    }

    public static void hidePlayerControl(HomeActivity parent) {
        if (parent != null) {
            parent.player.setVisibility(View.GONE);
            parent.ll_empty_player.setVisibility(View.VISIBLE);
        }
    }

}
