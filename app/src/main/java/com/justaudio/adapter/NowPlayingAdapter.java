package com.justaudio.adapter;

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
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.utils.CustomDialog;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.UILoader;
import com.justaudio.views.GifImageView;

/**
 * Created by ${VIDYA}
 */

public class NowPlayingAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private HomeActivity parent;

    public NowPlayingAdapter(HomeActivity context) {
        this.parent = context;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public int getCount() {
        return parent.playerList.size();
    }

    @Override
    public Object getItem(int position) {
        return parent.playerList.get(position);
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

        final TrackAudioModel mData = parent.playerList.get(position);

        holder.tv_list_title.setText(mData.getTitle());


        UILoader.UILPicLoading(holder.iv_list, mData.getThumbnail_image(), null, R.drawable.icon_list_holder);


        if (HomeActivity.pause_button_position == position) {
            holder.iv_list_play.setVisibility(View.VISIBLE);
        } else
            holder.iv_list_play.setVisibility(View.GONE);


        /*PLAY THE AUDIO*/
        holder.ll_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.pause_button_position = position;
                parent.updateCurrentUI();
                parent.player.playAudio(mData);
            }
        });

        /*MORE BUTTON*/
        holder.iv_list_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.showMoreRemoveDialog(parent, mData, position);
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