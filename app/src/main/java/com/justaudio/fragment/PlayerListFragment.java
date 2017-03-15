package com.justaudio.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.justaudio.dto.MovieInfoModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.interfaces.IUpdateUi;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.UILoader;
import com.justaudio.utils.Utils;

import java.util.ArrayList;


/**
 * Created by VIDYA
 */
public class PlayerListFragment extends Fragment implements IUpdateUi {

    private View view;
    private HomeActivity parent;

    private ListView listView;
    private int mPosition = -1;
    private String tabName;
    public ArrayList<TrackAudioModel> results;

    private static IUpdateUi iUpdateUi;
    private PlayerListFragment fragment;

    public static IUpdateUi getInstance() {
        return iUpdateUi;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iUpdateUi = this;
        //model = (MovieInfoModel) getArguments().getSerializable(AppConstants.INTENT_KEY_OBJECT_MOVIE);
        mPosition = getArguments().getInt("Position");
        tabName = getArguments().getString("tabName");
        parent = (HomeActivity) getActivity();
        fragment = PlayerListFragment.this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*if (view != null)
            return view;*/

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


        results = parent.audioModel.getTabList().get(mPosition).getAudioList();
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
            adapter = new PlayerListAdapter(parent/*, results*/);
            listView.setAdapter(adapter);
        } else
            adapter.updateAdapter(results);

        if (mPosition == 0 && parent.player != null)
            parent.player.initPlaylist(results);
    }


    private int pause_button_position = -1;

    @Override
    public void updateUI() {
        if (pause_button_position == results.size() - 1) {
            pause_button_position = 0;
            //PlayerListFragment.adapter.notifyDataSetChanged();
        } else {
            pause_button_position = pause_button_position + 1;
            //PlayerListFragment.adapter.notifyDataSetChanged();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updatePreUI() {
        if (pause_button_position == 0) {
            pause_button_position = results.size() - 1;
            //PlayerListFragment.adapter.notifyDataSetChanged();
        } else {
            pause_button_position = pause_button_position - 1;
            //PlayerListFragment.adapter.notifyDataSetChanged();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateCurrentUI() {
        pause_button_position = -1;
        adapter.notifyDataSetChanged();
    }

    private class PlayerListAdapter extends BaseAdapter {

        //private ArrayList<TrackAudioModel> dataList;
        private LayoutInflater inflater;
        private HomeActivity parent;

        private PlayerListAdapter(HomeActivity context/*, ArrayList<TrackAudioModel> results*/) {
            //this.dataList = results;
            parent = context;
            inflater = LayoutInflater.from(context.getApplicationContext());
        }


        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results.get(position);
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

            TrackAudioModel mData = results.get(position);

            holder.tv_list_title.setText(mData.getTitle());


            /*FOR ARTIEST TYPE*/
            if (tabName.equalsIgnoreCase("Artists")) {
                UILoader.UILPicLoading(holder.iv_list, mData.getThumbnail_image(), holder.pb_list, R.drawable.icon_list_holder);
            } else
                UILoader.UILPicLoading(holder.iv_list, mData.getThumbnail_image(), holder.pb_list,
                        R.drawable.icon_list_holder);

            if (pause_button_position == position) {
                holder.iv_list_play.setImageResource(R.drawable.icon_stop);
            } else {
                holder.iv_list_play.setImageResource(R.drawable.icon_play);
            }

            /*PLAY THE AUDIO*/
            holder.iv_list_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*CHANGE ICON POSITION*/
                    pause_button_position = position;
                    //holder.iv_list_play.setImageResource(R.drawable.icon_stop);
                    notifyDataSetChanged();

                    TrackAudioModel mData = results.get(position);
                    //Log.d("getTargetFragment", "" + getTargetFragment().getTargetRequestCode());
                    parent.player.playAudio(mData, PlayerFragment.fragmentArrayList.get(mPosition));
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
            //this.dataList = result;
            listView.setAdapter(null);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }
}
