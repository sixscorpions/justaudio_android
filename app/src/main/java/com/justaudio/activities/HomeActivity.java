package com.justaudio.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.adapter.LeftMenuAdapter;
import com.justaudio.adapter.NowPlayingAdapter;
import com.justaudio.audioplayer.AudioPlayerView;
import com.justaudio.dto.LeftMenuModel;
import com.justaudio.dto.MovieInfoModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.fragment.HomeFragment;
import com.justaudio.fragment.PlayerFragment;
import com.justaudio.interfaces.IUpdateUi;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.AudioUtils;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.Utils;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity implements IUpdateUi {

    public DrawerLayout drawer_layout;
    public AudioPlayerView player;

    public LinearLayout ll_empty_player;
    public LinearLayout ll_playing;
    private ImageView iv_now_playing_close;

    private ListView lv_player;
    public NowPlayingAdapter adapter;

    public MovieInfoModel audioModel;
    public ArrayList<TrackAudioModel> playerList;
    public int pause_button_position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ll_playing = (LinearLayout) findViewById(R.id.ll_playing);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_layout.setFocusable(true);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        /*NOW PLAYING LAYOUT*/
        lv_player = (ListView) findViewById(R.id.listView);
        TextView tv_now_playing = (TextView) findViewById(R.id.tv_now_playing);
        tv_now_playing.setTypeface(FontFamily.setHelveticaTypeface(this), Typeface.BOLD);
        iv_now_playing_close = (ImageView) findViewById(R.id.iv_now_playing_close);
        iv_now_playing_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_playing.setVisibility(View.GONE);
                drawer_layout.setVisibility(View.VISIBLE);
            }
        });


        ll_empty_player = (LinearLayout) findViewById(R.id.ll_empty_player);
        player = (AudioPlayerView) findViewById(R.id.player_view);
        player.setInitializeActivity(this);
        AudioUtils.hidePlayerControl(this);

        FrameLayout fl_player = (FrameLayout) findViewById(R.id.fl_player);
        fl_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getVisibility() == View.VISIBLE) {
                    ll_playing.setVisibility(View.VISIBLE);
                    drawer_layout.setVisibility(View.GONE);
                    setPlayerListData();
                }
            }
        });
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


    public void setPlayerListData() {
        if (adapter == null) {
            adapter = new NowPlayingAdapter(this);
            lv_player.setAdapter(adapter);
        } else {
            lv_player.setAdapter(null);
            adapter.notifyDataSetChanged();
            lv_player.setAdapter(adapter);
        }
    }

    @Override
    public void updateUI() {

        if (pause_button_position == playerList.size() - 1)
            pause_button_position = 0;
        else
            pause_button_position = pause_button_position + 1;

        adapter.notifyDataSetChanged();
    }

    @Override
    public void updatePreUI() {

        if (pause_button_position == 0)
            pause_button_position = playerList.size() - 1;
        else
            pause_button_position = pause_button_position - 1;

        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateCurrentUI() {

        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {

        if (ll_playing.getVisibility() == View.VISIBLE) {
            iv_now_playing_close.performClick();
        }

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
