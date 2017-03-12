package com.justaudio.services;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.BaseActivity;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.Utils;


/**
 * Created by ${VIDYA}
 */
public class NetworkUtils {



  /*  public static boolean isNetworkAvailable(BaseActivity parent) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) parent.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }*/

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }

    @SuppressLint("SetTextI18n")
    public static Dialog showNetworkConnectDialog(final BaseActivity context, boolean isNoInternet) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_network_check);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);


        TextView tv_alert_dialog_title = (TextView) mDialog.findViewById(R.id.tv_alert_dialog_title);
        tv_alert_dialog_title.setTypeface(FontFamily.setHelveticaTypeface(context), Typeface.BOLD);
        if (isNoInternet)
            tv_alert_dialog_title.setText(Utils.getStrings(context, R.string.no_internet_title));
        else
            tv_alert_dialog_title.setText(Utils.getStrings(context, R.string.app_name));

        TextView tv_dialog_content = (TextView) mDialog.findViewById(R.id.tv_dialog_content);
        tv_dialog_content.setTypeface(FontFamily.setHelveticaTypeface(context));
        if (isNoInternet)
            tv_dialog_content.setText(Utils.getStrings(context, R.string.no_internet_msg_main));
        else
            tv_dialog_content.setText(Utils.getStrings(context, R.string.no_date));


        Button btn_dialog_cancel = (Button) mDialog.findViewById(R.id.btn_dialog_cancel);
        btn_dialog_cancel.setTypeface(FontFamily.setHelveticaTypeface(context), Typeface.BOLD);
        btn_dialog_cancel.setText("OK");
        btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                context.finish();
            }
        });


        Button btn_dialog_ok = (Button) mDialog.findViewById(R.id.btn_dialog_ok);
        if (isNoInternet) {
            btn_dialog_ok.setVisibility(View.VISIBLE);
            btn_dialog_ok.setTypeface(FontFamily.setHelveticaTypeface(context), Typeface.BOLD);
            btn_dialog_ok.setText("Settings");
            btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(
                            android.provider.Settings.ACTION_SETTINGS));
                    mDialog.dismiss();
                }
            });
        } else
            btn_dialog_ok.setVisibility(View.GONE);


        mDialog.show();
        return mDialog;
    }


    public static Dialog showAlertDialog(final BaseActivity context, String msg) {
        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_network_check);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);

        TextView tv_alert_dialog_title = (TextView) mDialog.findViewById(R.id.tv_alert_dialog_title);
        tv_alert_dialog_title.setTypeface(FontFamily.setHelveticaTypeface(context), Typeface.BOLD);
        tv_alert_dialog_title.setText("" + Utils.getStrings(context, R.string.app_name));

        TextView tv_dialog_content = (TextView) mDialog.findViewById(R.id.tv_dialog_content);
        tv_dialog_content.setTypeface(FontFamily.setHelveticaTypeface(context));
        tv_dialog_content.setText("" + msg);


        Button btn_dialog_cancel = (Button) mDialog.findViewById(R.id.btn_dialog_cancel);
        btn_dialog_cancel.setTypeface(FontFamily.setHelveticaTypeface(context), Typeface.BOLD);
        btn_dialog_cancel.setText("OK");
        btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                context.onBackPressed();
            }
        });


        Button btn_dialog_ok = (Button) mDialog.findViewById(R.id.btn_dialog_ok);
        btn_dialog_ok.setVisibility(View.GONE);

        mDialog.show();
        return mDialog;
    }


}
