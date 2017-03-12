package com.justaudio.utils;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.justaudio.R;
import com.justaudio.activities.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ${VIDYA}
 */

public class CustomDialog {


    /*SHOW PROGRESS BAR*/
    public static Dialog showProgressDialog(BaseActivity baseActivity, boolean cancelable) {

        Dialog mDialog = new Dialog(baseActivity);
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
}
