package com.justaudio.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.justaudio.dto.MovieListModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.fragment.FavoritesFragment;
import com.justaudio.utils.CustomDialog;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.UILoader;

import java.util.ArrayList;

/**
 * Created by ${VIDYA}
 */

public class FavoritesAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private HomeActivity parent;
    private ArrayList<TrackAudioModel> playerList;

    private FavoritesFragment favoritesFragment;

    public FavoritesAdapter(HomeActivity context, ArrayList<TrackAudioModel> data,
                            FavoritesFragment fragment) {
        inflater = LayoutInflater.from(context.getApplicationContext());

        this.parent = context;
        this.favoritesFragment = fragment;
        playerList = data;
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
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup view) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.row_artist_list_items, view, false);

            holder.ll_click = (LinearLayout) convertView.findViewById(R.id.ll_click);

            //IMAGE_VIEW AND PROGRESS BAR
            holder.iv_list = (ImageView) convertView.findViewById(R.id.iv_list);
            holder.pb_list = (ProgressBar) convertView.findViewById(R.id.pb_list);


            holder.iv_list_play = (ImageView) convertView.findViewById(R.id.iv_list_play);

            holder.iv_list_more = (ImageView) convertView.findViewById(R.id.iv_list_more);

            //TILE TEXT
            holder.tv_list_title = (TextView) convertView.findViewById(R.id.tv_list_title);
            holder.tv_list_title.setTypeface(FontFamily.setHelveticaTypeface(parent));

            //SUB_TITLE
            holder.tv_list_subtext = (TextView) convertView.findViewById(R.id.tv_list_subtext);
            holder.tv_list_subtext.setTypeface(FontFamily.setHelveticaTypeface(parent));

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final TrackAudioModel mData = playerList.get(position);

        holder.tv_list_title.setText(mData.getTitle());

        UILoader.UILPicLoading(holder.iv_list, mData.getThumbnail_image(), holder.pb_list,
                R.drawable.icon_list_holder);


        /*UPDATE PLAY AND PAUSE BUTTON*/
        if (favoritesFragment.pause_button_position == position)
            holder.iv_list_play.setImageResource(R.drawable.icon_stop);
        else
            holder.iv_list_play.setImageResource(R.drawable.icon_play);


        /*PLAY THE AUDIO*/
        holder.ll_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                favoritesFragment.pause_button_position = position;
                favoritesFragment.updateCurrentUI();

                parent.player.initPlaylist(playerList, favoritesFragment);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        parent.player.playAudio(mData);
                    }
                }, 100);
            }
        });

        /*MORE BUTTON*/
        holder.iv_list_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.showRemoveFevDialog(parent, mData);
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

        LinearLayout ll_click;
    }

}