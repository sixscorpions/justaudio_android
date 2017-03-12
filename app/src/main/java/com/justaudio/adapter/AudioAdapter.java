package com.justaudio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.AudioPlayerActivity;
import com.justaudio.dto.TrackAudioModel;

import java.util.List;

/**
 * Created by jean
 */

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioAdapterViewHolder> implements View.OnClickListener {
    private Context context;
    private AudioPlayerActivity activity;
    private List<TrackAudioModel> trackAudioModelList;

    public AudioAdapter(AudioPlayerActivity activity) {
        this.activity = activity;
        this.context = activity;
    }

    @Override
    public AudioAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.row_audio_list_items, parent, false);
        AudioAdapterViewHolder audiosViewHolder = new AudioAdapterViewHolder(view);
        audiosViewHolder.itemView.setOnClickListener(this);
        return audiosViewHolder;
    }

    @Override
    public void onBindViewHolder(AudioAdapterViewHolder holder, int position) {
        String title = position + 1 + "" + trackAudioModelList.get(position).getTitle();
        holder.tv_list_title.setText(title);
        holder.itemView.setTag(trackAudioModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return trackAudioModelList == null ? 0 : trackAudioModelList.size();
    }

    public void setupItems(List<TrackAudioModel> trackAudioModelList) {
        this.trackAudioModelList = trackAudioModelList;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    static class AudioAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tv_list_title;
        TextView tv_list_subtext;

        ImageView iv_list;
        ProgressBar pb_list;

        private AudioAdapterViewHolder(View view) {
            super(view);

            this.tv_list_title = (TextView) view.findViewById(R.id.tv_list_title);
            this.tv_list_subtext = (TextView) view.findViewById(R.id.tv_list_subtext);

            /*IMAGE_VIEW AND PROGRESS BAR*/
            this.iv_list = (ImageView) view.findViewById(R.id.iv_list);
            this.pb_list = (ProgressBar) view.findViewById(R.id.pb_list);
        }
    }
}
