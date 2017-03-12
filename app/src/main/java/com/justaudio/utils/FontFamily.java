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
     * ARIAL REGULAR
     **/
    public static Typeface setHelveticaTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica_Reg.ttf");
    }


    /*
    SET THE VIEWPAGER TYPE FACE
    * */
    public static void setViewPageTypeface(BaseActivity parent, TabLayout tabLayout) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(FontFamily.setHelveticaTypeface(parent), Typeface.BOLD);
                }
            }
        }
    }


}
