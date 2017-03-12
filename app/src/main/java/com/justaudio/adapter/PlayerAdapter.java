package com.justaudio.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.AudioPlayerActivity;
import com.justaudio.activities.BaseActivity;
import com.justaudio.dto.MovieListModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.fragment.PlayerListFragment;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.UILoader;
import com.justaudio.utils.Utils;

import java.util.ArrayList;

/**
 * Created by VIDYA
 */

public class PlayerAdapter extends ArrayAdapter<TrackAudioModel> {

    private ArrayList<TrackAudioModel> dataList;
    private LayoutInflater inflater;
    private AudioPlayerActivity parent;


    public PlayerAdapter(AudioPlayerActivity context, int id, ArrayList<TrackAudioModel> results) {
        super(context, id);
        this.dataList = results;
        parent = context;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public TrackAudioModel getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getPosition(TrackAudioModel item) {
        return dataList.indexOf(item);
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup view) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_audio_list_items, null);

            /*IMAGE_VIEW AND PROGRESS BAR*/
            holder.iv_list = (ImageView) convertView.findViewById(R.id.iv_list);
            holder.pb_list = (ProgressBar) convertView.findViewById(R.id.pb_list);

            /*TILE TEXT */
            holder.tv_list_title = (TextView) convertView.findViewById(R.id.tv_list_title);
            holder.tv_list_title.setTypeface(FontFamily.setHelveticaTypeface(parent));

            /*SUB_TITLE */
            holder.tv_list_subtext = (TextView) convertView.findViewById(R.id.tv_list_subtext);
            holder.tv_list_subtext.setTypeface(FontFamily.setHelveticaTypeface(parent));

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        TrackAudioModel mData = dataList.get(position);

        holder.tv_list_title.setText(mData.getTitle());

        UILoader.UILPicLoading(holder.iv_list, mData.getThumbnail_image(),
                holder.pb_list, R.drawable.ic_gallery_placeholder);

        return convertView;
    }


    private class ViewHolder {
        ImageView iv_list;
        ProgressBar pb_list;

        TextView tv_list_title;
        TextView tv_list_subtext;
    }

    public void updateAdapter(ArrayList<TrackAudioModel> result) {
        dataList.clear();
        dataList.addAll(result);
        notifyDataSetChanged();
    }

}

