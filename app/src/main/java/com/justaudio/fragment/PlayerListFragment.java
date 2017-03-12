package com.justaudio.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.AudioPlayerActivity;
import com.justaudio.dto.MovieInfoModel;
import com.justaudio.dto.TabListModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.UILoader;
import com.justaudio.utils.Utils;

import java.util.ArrayList;


/**
 * Created by Pavan
 */
public class PlayerListFragment extends Fragment {

    private View view;
    private AudioPlayerActivity parent;

    private ListView listView;
    private int position = -1;
    private MovieInfoModel model;
    private String tabName;
    public ArrayList<TrackAudioModel> results;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = (MovieInfoModel) getArguments().getSerializable(AppConstants.INTENT_KEY_OBJECT_MOVIE);
        position = getArguments().getInt("Position");
        tabName = getArguments().getString("tabName");

        parent = (AudioPlayerActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_list_child_new, container, false);
        initializeTheViews();
        return view;
    }


    private void initializeTheViews() {

        /*LIST VIEW*/
        listView = (ListView) view.findViewById(R.id.listView);

        /*TEXT  NO _RESULT*/
        TextView tv_no_result = (TextView) view.findViewById(R.id.tv_no_result);
        tv_no_result.setTypeface(FontFamily.setHelveticaTypeface(parent));
        tv_no_result.setTextColor(Utils.getColor(parent, R.color.white));


        results = model.getTabList().get(position).getAudioList();
        if (results.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            tv_no_result.setVisibility(View.GONE);
            setListData(results);
        } else {
            listView.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
        }
    }

    private PlayerListAdapter adapter;

    private void setListData(ArrayList<TrackAudioModel> results) {

        if (adapter == null) {
            adapter = new PlayerListAdapter(parent, results);
            listView.setAdapter(adapter);
        } else
            adapter.updateAdapter(results);

        if (parent.player != null)
            parent.player.initPlaylist(results);
    }


    private int pause_button_position = -1;

    private class PlayerListAdapter extends BaseAdapter {

        private ArrayList<TrackAudioModel> dataList;
        private LayoutInflater inflater;
        private AudioPlayerActivity parent;


        private PlayerListAdapter(AudioPlayerActivity context, ArrayList<TrackAudioModel> results) {
            this.dataList = results;
            parent = context;
            inflater = LayoutInflater.from(context.getApplicationContext());
        }


        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @SuppressLint("InflateParams")
        public View getView(final int position, View convertView, ViewGroup view) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                if (tabName.equalsIgnoreCase("Artists"))
                    convertView = inflater.inflate(R.layout.row_artist_list_items, null);
                else
                    convertView = inflater.inflate(R.layout.row_audio_list_items, null);

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

            if (position != pause_button_position)
                holder.iv_list_play.setImageResource(R.drawable.icon_play_small);

            TrackAudioModel mData = dataList.get(position);

            holder.tv_list_title.setText(mData.getTitle());


            /*FOR ARTIEST TYPE*/
            if (tabName.equalsIgnoreCase("Artists")) {
                String url = "http://nas01.atnoc.com/cineaaudio/Srimanthudu/moviecharacters/mahesh.jpg";
                UILoader.UILPicLoading(holder.iv_list, url, holder.pb_list, R.drawable.icon_list_holder);
            } else
                UILoader.UILPicLoading(holder.iv_list, mData.getThumbnail_image(), holder.pb_list,
                        R.drawable.icon_list_holder);


            /*PLAY THE AUDIO*/
            holder.iv_list_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*CHANGE ICON POSITION*/
                    pause_button_position = position;
                    holder.iv_list_play.setImageResource(R.drawable.icon_player_pause);
                    notifyDataSetChanged();


                    TrackAudioModel mData = dataList.get(position);
                    parent.player.playAudio(mData);
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

        void updateAdapter(ArrayList<TrackAudioModel> result) {
            this.dataList = result;
            listView.setAdapter(null);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }
}
