package com.justaudio.utils;

import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.justaudio.R;
import com.justaudio.activities.BaseActivity;
import com.justaudio.dto.LeftMenuModel;

import java.util.ArrayList;

/**
 * Created by ${VIDYA}
 */

public class ToolbarUtils {


    /**
     * MAKE THE STATUS BAR TRANSLATE
     */
    public static void setTranslateStatusBar(BaseActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Utils.getColor(activity, R.color.black_transparent));
        }
    }


    /**
     * DETAIL TOOLBAR
     */
    public static void setAudioPlayerToolbar(final BaseActivity parent) {

        ImageView iv_action_back = (ImageView) parent.findViewById(R.id.iv_action_back);
        iv_action_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parent.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = parent.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Utils.getColor(parent, R.color.black_transparent));
        }
    }

    public static LinearLayout initializeNoDataView(BaseActivity parent, View view) {
        /*LAYOUT NO DATA*/
        LinearLayout ll_no_data = (LinearLayout) view.findViewById(R.id.ll_no_data);

        TextView tv_no_data = (TextView) view.findViewById(R.id.tv_no_data);
        tv_no_data.setTypeface(FontFamily.setHelveticaTypeface(parent), Typeface.BOLD);
        return ll_no_data;
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
                    TextView tvTab = ((TextView) tabViewChild);
                    tvTab.setTypeface(FontFamily.setHelveticaTypeface(parent), Typeface.BOLD);
                    tvTab.setPadding(30, 5, 30, 5);
                }
            }
        }
    }


    /*LEFT MENU ITEMS LIST*/
    public static ArrayList<LeftMenuModel> getLeftMenuList() {
        ArrayList<LeftMenuModel> mLeftMenu = new ArrayList<>();
        mLeftMenu.add(new LeftMenuModel("Home"));
        mLeftMenu.add(new LeftMenuModel("New Releases"));
        mLeftMenu.add(new LeftMenuModel("Playlists"));
        mLeftMenu.add(new LeftMenuModel("Favorites"));
        mLeftMenu.add(new LeftMenuModel("Settings"));
        mLeftMenu.add(new LeftMenuModel("Share"));
        mLeftMenu.add(new LeftMenuModel("Rate Us"));
        return mLeftMenu;
    }

}
