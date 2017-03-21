package com.justaudio.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.utils.CustomDialog;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.UILoader;
import com.justaudio.utils.Utils;

import java.util.ArrayList;


/**
 * Created by VIDYA
 */
public class PlayerListFragment extends Fragment {

    private View view;
    private HomeActivity parent;

    private ListView listView;
    private PlayerListAdapter adapter;
    private int mPosition = -1;
    private ArrayList<TrackAudioModel> results;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("Position");
        parent = (HomeActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
            setListData();
        } else {
            listView.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
        }
    }


    private void setListData() {

        if (adapter == null) {
            adapter = new PlayerListAdapter(parent);
            listView.setAdapter(adapter);
        } else
            adapter.updateAdapter();
    }


    private class PlayerListAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private HomeActivity parent;

        private PlayerListAdapter(HomeActivity context) {
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
            return position;
        }


        @SuppressLint("InflateParams")
        public View getView(final int position, View convertView, ViewGroup view) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();


                convertView = inflater.inflate(R.layout.row_artist_list_items, null);

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


            final TrackAudioModel mData = results.get(position);

            holder.tv_list_title.setText(mData.getTitle());
            UILoader.UILPicLoading(holder.iv_list, mData.getThumbnail_image(),
                    holder.pb_list, R.drawable.icon_list_holder);


            /*PLAY THE AUDIO*/
            holder.ll_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    parent.pause_button_position = position;

                    parent.player.initPlaylist(results, null);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TrackAudioModel mData = results.get(position);
                            parent.player.playAudio(mData);
                        }
                    }, 100);
                }
            });


            /*MORE BUTTON*/
            holder.iv_list_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialog.showMoreAddDialog(parent, mData);
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

        void updateAdapter() {
            listView.setAdapter(null);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }
}
