package com.justaudio.activities;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.adapter.LeftMenuAdapter;
import com.justaudio.audioplayer.AudioPlayerView;
import com.justaudio.dto.LeftMenuModel;
import com.justaudio.dto.MovieInfoModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.fragment.HomeFragment;
import com.justaudio.fragment.PlayerFragment;
import com.justaudio.fragment.PlayerListFragment;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.AudioUtils;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.UILoader;
import com.justaudio.utils.Utils;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity {

    public DrawerLayout drawer_layout;
    public AudioPlayerView player;

    public LinearLayout ll_empty_player;
    public LinearLayout ll_playing;

    private ListView lv_player;
    public PlayerListAdapter adapter;

    public MovieInfoModel audioModel;
    public ArrayList<TrackAudioModel> playerList;

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
        ImageView iv_now_playing_close = (ImageView) findViewById(R.id.iv_now_playing_close);
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
            adapter = new PlayerListAdapter(this);
            lv_player.setAdapter(adapter);
        } else
            adapter.updateAdapter();
    }


    private class PlayerListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private PlayerListAdapter(HomeActivity context) {
            inflater = LayoutInflater.from(context.getApplicationContext());
        }

        @Override
        public int getCount() {
            return playerList.size();
        }

        @Override
        public Object getItem(int position) {
            return playerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        public View getView(final int position, View convertView, ViewGroup view) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.row_audio_list_items, view, false);

                //IMAGE_VIEW AND PROGRESS BAR
                holder.iv_list = (ImageView) convertView.findViewById(R.id.iv_list);
                holder.pb_list = (ProgressBar) convertView.findViewById(R.id.pb_list);


                holder.iv_list_play = (ImageView) convertView.findViewById(R.id.iv_list_play);

                holder.iv_list_more = (ImageView) convertView.findViewById(R.id.iv_list_more);

                //TILE TEXT
                holder.tv_list_title = (TextView) convertView.findViewById(R.id.tv_list_title);
                holder.tv_list_title.setTypeface(FontFamily.setHelveticaTypeface(HomeActivity.this));

                //SUB_TITLE
                holder.tv_list_subtext = (TextView) convertView.findViewById(R.id.tv_list_subtext);
                holder.tv_list_subtext.setTypeface(FontFamily.setHelveticaTypeface(HomeActivity.this));

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            TrackAudioModel mData = playerList.get(position);

            holder.tv_list_title.setText(mData.getTitle());

            UILoader.UILPicLoading(holder.iv_list, mData.getThumbnail_image(), holder.pb_list,
                    R.drawable.icon_list_holder);


            /*PLAY THE AUDIO*/
            holder.iv_list_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView iv_list;
            ImageView iv_list_play;
            ImageView iv_list_more;

            ProgressBar pb_list;

            TextView tv_list_title;
            TextView tv_list_subtext;
        }

        void updateAdapter() {
            lv_player.setAdapter(null);
            adapter.notifyDataSetChanged();
            lv_player.setAdapter(adapter);
        }
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
