package com.justaudio.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.justaudio.R;
import com.justaudio.adapter.LeftMenuAdapter;
import com.justaudio.adapter.NowPlayingAdapter;
import com.justaudio.audioplayer.AudioPlayerView;
import com.justaudio.audioplayer.PlayerNotificationReceiver;
import com.justaudio.dto.LeftMenuModel;
import com.justaudio.dto.MovieInfoModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.fragment.FavoritesFragment;
import com.justaudio.fragment.HomeFragment;
import com.justaudio.fragment.PlayerFragment;
import com.justaudio.services.ApiConfiguration;
import com.justaudio.services.JSONFevTask;
import com.justaudio.services.JSONResult;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.AudioUtils;
import com.justaudio.utils.CustomDialog;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity implements JSONResult {

    public DrawerLayout drawer_layout;
    public AudioPlayerView player;

    public LinearLayout ll_empty_player;
    private LinearLayout ll_playing;
    public ImageView iv_now_playing_close;

    private ListView lv_player;
    public static NowPlayingAdapter adapter;

    public MovieInfoModel audioModel;
    public ArrayList<TrackAudioModel> playerList;
    public static int pause_button_position = -1;
    public FrameLayout fl_player;

    public static AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


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


        fl_player = (FrameLayout) findViewById(R.id.fl_player);
        fl_player.setVisibility(View.GONE);
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

        ll_playing = (LinearLayout) findViewById(R.id.ll_playing);
        ll_empty_player = (LinearLayout) findViewById(R.id.ll_empty_player);


        player = (AudioPlayerView) findViewById(R.id.player_view);
        player.setInitializeActivity(this);
        AudioUtils.hidePlayerControl(this);


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

                String currentTitle = item.getMenuTitle();
                switch (currentTitle) {
                    case "Home":
                        navigateHomeFragment(currentTitle);
                        break;
                    case "Favorites":
                        navigateFavoriteFragment(currentTitle);
                        break;
                }

            }
        });
        lv_left_menu.performItemClick(lv_left_menu, 0, 0);

        /*TEXT VERSION NUMBER*/
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setTypeface(FontFamily.setHelveticaTypeface(this), Typeface.BOLD);
    }

    private void navigateHomeFragment(String currentTitle) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.INTENT_KEY_TITLE, currentTitle);
        Utils.navigateFragment(new HomeFragment(), HomeFragment.TAG, bundle, this);
    }

    private void navigateFavoriteFragment(String currentTitle) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.INTENT_KEY_TITLE, currentTitle);
        Utils.navigateFragment(new FavoritesFragment(), FavoritesFragment.TAG, bundle, this);
    }


    private void setPlayerListData() {
        if (adapter == null) {
            adapter = new NowPlayingAdapter(this);
            lv_player.setAdapter(adapter);
        } else {
            lv_player.setAdapter(null);
            adapter.notifyDataSetChanged();
            lv_player.setAdapter(adapter);
        }
    }

    public void updateNextUI() {

        if (pause_button_position == playerList.size() - 1)
            pause_button_position = 0;
        else
            pause_button_position = pause_button_position + 1;

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void updatePreUI() {

        if (pause_button_position == 0)
            pause_button_position = playerList.size() - 1;
        else
            pause_button_position = pause_button_position - 1;

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void updateCurrentUI() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void addToFavorites(long audioId) {

        CustomDialog.showProgressDialog(this, false);

        String deviceId = Utils.getDeviceID(this);
        String url = String.format(Utils.getServer(this, R.string.REST_ADD_TO_FAVORITES), deviceId) + audioId;


        JSONFevTask jsonDetailTask = new JSONFevTask(this);
        jsonDetailTask.setMethod(JSONFevTask.METHOD.POST);
        jsonDetailTask.setParams(new JSONObject());
        jsonDetailTask.setCode(ApiConfiguration.REST_ADD_TO_FAVORITES_CODE);
        jsonDetailTask.setServerUrl(url);
        jsonDetailTask.setErrorMessage(ApiConfiguration.ERROR_RESPONSE_CODE);
        jsonDetailTask.setConnectTimeout(8000);
        jsonDetailTask.execute();
    }


    public void deleteFromFavorites(long audioId) {

        CustomDialog.showProgressDialog(this, false);

        String deviceId = Utils.getDeviceID(this);
        String url = String.format(Utils.getServer(this, R.string.REST_DELETE_FROM_FAVORITES), deviceId) + audioId;

        JSONFevTask jsonDetailTask = new JSONFevTask(this);
        jsonDetailTask.setMethod(JSONFevTask.METHOD.DELETE);
        jsonDetailTask.setParams(new JSONObject());
        jsonDetailTask.setCode(ApiConfiguration.REST_DELETE_FROM_FAVORITES_CODE);
        jsonDetailTask.setServerUrl(url);
        jsonDetailTask.setErrorMessage(ApiConfiguration.ERROR_RESPONSE_CODE);
        jsonDetailTask.setConnectTimeout(8000);
        jsonDetailTask.execute();
    }

    @Override
    public void successJsonResult(int code, Object result) {

        if (code == ApiConfiguration.REST_ADD_TO_FAVORITES_CODE) {
            String status = (String) result;
            if (status.equalsIgnoreCase("true"))
                Toast.makeText(this, "Song Added to Favorites", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Failed to Add Song", Toast.LENGTH_SHORT).show();

        }

        if (code == ApiConfiguration.REST_DELETE_FROM_FAVORITES_CODE) {
            String status = (String) result;
            if (status.equalsIgnoreCase("true")) {
                updateRemovedFevAdapter();
                Toast.makeText(this, "Song Deleted from Favorites", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Failed to Delete", Toast.LENGTH_SHORT).show();
        }
        CustomDialog.hideProgressBar(this);
    }


    private void updateRemovedFevAdapter() {
        FavoritesFragment fragment = (FavoritesFragment) getSupportFragmentManager().
                findFragmentByTag(FavoritesFragment.TAG);
        fragment.getMoviesData();
    }

    @Override
    public void failedJsonResult(int code) {

        if (code == ApiConfiguration.REST_ADD_TO_FAVORITES_CODE)
            Toast.makeText(this, "Failed to Add Song", Toast.LENGTH_SHORT).show();

        if (code == ApiConfiguration.REST_DELETE_FROM_FAVORITES_CODE)
            Toast.makeText(this, "Failed to Delete", Toast.LENGTH_SHORT).show();

        CustomDialog.hideProgressBar(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null)
            player.kill();
    }

    private int closeCount;

    @Override
    public void onBackPressed() {

        if (ll_playing.getVisibility() == View.VISIBLE)
            iv_now_playing_close.performClick();


        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry backStackEntry
                    = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            String fragName = backStackEntry.getName();
            switch (fragName) {
                case HomeFragment.TAG:
                    closeCount++;
                    if (closeCount > 1)
                        minimizeApp();
                    break;
                case PlayerFragment.TAG:
                    super.onBackPressed();
                    break;
                case FavoritesFragment.TAG:
                    super.onBackPressed();
                    break;
                default:
                    finish();
                    break;
            }
        } else
            finish();
    }


    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
