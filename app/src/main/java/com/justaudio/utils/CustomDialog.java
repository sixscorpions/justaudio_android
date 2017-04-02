package com.justaudio.utils;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.justaudio.R;
import com.justaudio.activities.BaseActivity;
import com.justaudio.activities.HomeActivity;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.services.ApiConfiguration;
import com.justaudio.services.JSONTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${VIDYA}
 */

public class CustomDialog {


    /*SHOW PROGRESS BAR*/
    public static Dialog showProgressDialog(BaseActivity baseActivity, boolean cancelable) {

        Dialog mDialog = new Dialog(baseActivity, R.style.NewDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater mInflater = LayoutInflater.from(baseActivity);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View layout = mInflater.inflate(R.layout.progressbar_custom, null);
        mDialog.setContentView(layout);

        TextView tvProgressMessage = (TextView) layout.findViewById(R.id.tvProgressMessage);
        tvProgressMessage.setTypeface(FontFamily.setHelveticaTypeface(baseActivity));
        tvProgressMessage.setVisibility(View.GONE);

        if (baseActivity.progressDialog != null) {
            baseActivity.progressDialog.dismiss();
            baseActivity.progressDialog = null;
        }

        baseActivity.progressDialog = mDialog;

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();

        return mDialog;
    }

    /*HIDE THE PROGRESS DIALOG*/
    public static void hideProgressBar(BaseActivity parent) {
        if (parent.progressDialog != null)
            parent.progressDialog.dismiss();
    }

    public static void showMoreAddDialog(final HomeActivity activity, final TrackAudioModel mData) {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.anim_list_dialog;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_more);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        /*TITLE*/
        TextView tv_info_title = (TextView) dialog.findViewById(R.id.tv_info_title);
        tv_info_title.setTypeface(FontFamily.setHelveticaTypeface(activity), Typeface.BOLD);
        tv_info_title.setText("" + mData.getTitle());


        /*CANCEL*/
        ImageView iv_dialog_close = (ImageView) dialog.findViewById(R.id.iv_info_close);
        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        /*FEV.*/
        TextView tv_dialog_fev = (TextView) dialog.findViewById(R.id.tv_dialog_fev);
        tv_dialog_fev.setTypeface(FontFamily.setHelveticaTypeface(activity));
        tv_dialog_fev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addToFavorites(mData.getId());
                dialog.dismiss();
            }
        });


        /*ADD TO QUE*/
        TextView tv_dialog_add_que = (TextView) dialog.findViewById(R.id.tv_dialog_add_que);
        tv_dialog_add_que.setTypeface(FontFamily.setHelveticaTypeface(activity));
        tv_dialog_add_que.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity.player.getVisibility() == View.INVISIBLE) {
                    HomeActivity.pause_button_position = 0;
                    List<TrackAudioModel> playlist = new ArrayList<>();
                    playlist.add(mData);
                    activity.player.initPlaylist(playlist, null);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activity.player.playAudio(mData);
                        }
                    }, 100);
                } else
                    activity.player.addAudio(mData);
                dialog.dismiss();
            }
        });


        /*SHARE*/
        TextView tv_dialog_share = (TextView) dialog.findViewById(R.id.tv_dialog_share);
        tv_dialog_share.setTypeface(FontFamily.setHelveticaTypeface(activity));
        tv_dialog_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                shareContent(activity);
            }
        });


        dialog.show();
    }


    public static void showMoreRemoveDialog(final HomeActivity activity, final TrackAudioModel mData, final int position) {


        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.anim_list_dialog;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_more);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

       /*TITLE*/
        TextView tv_info_title = (TextView) dialog.findViewById(R.id.tv_info_title);
        tv_info_title.setTypeface(FontFamily.setHelveticaTypeface(activity), Typeface.BOLD);
        tv_info_title.setText("" + mData.getTitle());

        /*CANCEL*/
        ImageView iv_dialog_close = (ImageView) dialog.findViewById(R.id.iv_info_close);
        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        /*FEV.*/
        TextView tv_dialog_fev = (TextView) dialog.findViewById(R.id.tv_dialog_fev);
        tv_dialog_fev.setTypeface(FontFamily.setHelveticaTypeface(activity));
        tv_dialog_fev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addToFavorites(mData.getId());
                dialog.dismiss();
            }
        });

        /*REMOVE*/
        TextView tv_dialog_add_que = (TextView) dialog.findViewById(R.id.tv_dialog_add_que);
        tv_dialog_add_que.setText("Remove");
        tv_dialog_add_que.setTypeface(FontFamily.setHelveticaTypeface(activity));
        tv_dialog_add_que.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (HomeActivity.pause_button_position == position) {
                    HomeActivity.pause_button_position = -1;
                } else if (HomeActivity.pause_button_position > position) {
                    HomeActivity.pause_button_position = HomeActivity.pause_button_position - 1;
                }
                activity.player.removeAudio(mData);
            }
        });


        /*SHARE*/
        TextView tv_dialog_share = (TextView) dialog.findViewById(R.id.tv_dialog_share);
        tv_dialog_share.setTypeface(FontFamily.setHelveticaTypeface(activity));
        tv_dialog_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                shareContent(activity);
            }
        });


        dialog.show();
    }

    public static void showRemoveFevDialog(final HomeActivity activity, final TrackAudioModel mData) {


        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.anim_list_dialog;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_more);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        /*TITLE*/
        TextView tv_info_title = (TextView) dialog.findViewById(R.id.tv_info_title);
        tv_info_title.setTypeface(FontFamily.setHelveticaTypeface(activity), Typeface.BOLD);
        tv_info_title.setText("" + mData.getTitle());

        /*CANCEL*/
        ImageView iv_dialog_close = (ImageView) dialog.findViewById(R.id.iv_info_close);
        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        /*ADD TO QUE*/
        TextView tv_dialog_add_que = (TextView) dialog.findViewById(R.id.tv_dialog_add_que);
        tv_dialog_add_que.setTypeface(FontFamily.setHelveticaTypeface(activity));
        tv_dialog_add_que.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity.player.getVisibility() == View.INVISIBLE) {
                    HomeActivity.pause_button_position = 0;
                    List<TrackAudioModel> playlist = new ArrayList<>();
                    playlist.add(mData);
                    activity.player.initPlaylist(playlist, null);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activity.player.playAudio(mData);
                        }
                    }, 100);
                } else
                    activity.player.addAudio(mData);
                dialog.dismiss();
            }
        });



        /*REMOVE*/
        TextView tv_dialog_fev = (TextView) dialog.findViewById(R.id.tv_dialog_fev);
        tv_dialog_fev.setText("Remove from favorite");
        tv_dialog_fev.setTypeface(FontFamily.setHelveticaTypeface(activity));
        tv_dialog_fev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.deleteFromFavorites(mData.getId());
            }
        });


        /*SHARE*/
        TextView tv_dialog_share = (TextView) dialog.findViewById(R.id.tv_dialog_share);
        tv_dialog_share.setTypeface(FontFamily.setHelveticaTypeface(activity));
        tv_dialog_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                shareContent(activity);
            }
        });


        dialog.show();
    }


    public static void shareContent(BaseActivity parent) {

        String link = "https://play.google.com/store/apps/details?id=com.justaudio";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Check out " + Utils.getStrings(parent, R.string.app_name) + " for your smartphone." +
                "Download it today from " + "\n"+link;
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        parent.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void rateUsOurApp(BaseActivity parent) {
        Uri uri = Uri.parse("market://details?id=" + parent.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            parent.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            parent.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + parent.getPackageName())));
        }
    }


}
