package com.justaudio.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.adapter.LeftMenuAdapter;
import com.justaudio.audioplayer.AudioPlayerView;
import com.justaudio.dto.LeftMenuModel;
import com.justaudio.dto.MovieInfoModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.fragment.HomeFragment;
import com.justaudio.fragment.PlayerFragment;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.AudioUtils;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.Utils;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity {

    public DrawerLayout drawer_layout;
    public AudioPlayerView player;
    public LinearLayout ll_empty_player;
    public MovieInfoModel audioModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_layout.setFocusable(true);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        player = (AudioPlayerView) findViewById(R.id.player_view);
        player.setInitializeActivity(this);

        ll_empty_player = (LinearLayout) findViewById(R.id.ll_empty_player);

        // AudioUtils.hidePlayerControl(this);
        //navigateHomeFragment("Home");

        setLeftMenuData();
    }


    private void setLeftMenuData() {

        /*LIST VIEW*/
        ListView lv_left_menu = (ListView) findViewById(R.id.lv_left_menu);

        final LeftMenuAdapter leftMenuAdapter = new LeftMenuAdapter(this, ToolbarUtils.getLeftMenuList());
        lv_left_menu.setAdapter(leftMenuAdapter);
        lv_left_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawer_layout.closeDrawers();
                LeftMenuModel item = (LeftMenuModel) parent.getAdapter().getItem(position);
                String title = item.getMenuTitle();

                switch (title) {
                    case "Home":
                        navigateHomeFragment(title);
                        break;
                }

            }
        });
        lv_left_menu.performItemClick(lv_left_menu, 0, 0);

        /*TEXT VERSION NUMBER*/
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setTypeface(FontFamily.setHelveticaTypeface(this), Typeface.BOLD);
    }

    private void navigateHomeFragment(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.INTENT_KEY_TITLE, title);
        Utils.navigateFragment(new HomeFragment(), HomeFragment.TAG, bundle, this);
    }

    @Override
    public void onBackPressed() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry backStackEntry
                    = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            if (backStackEntry.getName().equalsIgnoreCase(PlayerFragment.TAG)) {
                super.onBackPressed();
            } else
                finish();
        } else
            finish();
    }
}
