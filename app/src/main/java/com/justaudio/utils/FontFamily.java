package com.justaudio.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justaudio.activities.BaseActivity;


/**
 * Created by ${VIDYA}
 */
public class FontFamily {


    /**
     * ROBOTO REGULAR
     **/
    public static Typeface setHelveticaTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/roboto_reguler.ttf");
    }


    public static String getServerString(String value) {
        String str;
        if (value == null || value.equals("") || value.equals("null")
                || value.trim().length() == 0) {
            str = "- -";
        } else
            str = value;
        return str;
    }
}
