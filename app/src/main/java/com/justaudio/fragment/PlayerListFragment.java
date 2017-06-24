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


        ArrayList<TrackAudioModel> results = parent.audioModel.getTabList()
                .get(mPosition).getAudioList();
        if (results.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            tv_no_result.setVisibility(View.GONE);
            setListData(results);
        } else {
            listView.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
        }
    }

    private void setListData(ArrayList<TrackAudioModel> results) {
        if (adapter == null) {
            PlayerListAdapter adapter = new PlayerListAdapter(parent, results);
            listView.setAdapter(adapter);
        } else
            adapter.updateAdapter(results);
    }


    private class PlayerListAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private HomeActivity parent;
        private ArrayList<TrackAudioModel> categoryList;

        private PlayerListAdapter(HomeActivity context, ArrayList<TrackAudioModel> results) {
            parent = context;
            inflater = LayoutInflater.from(context.getApplicationContext());
            categoryList = results;
        }


        @Override
        public int getCount() {
            return categoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return categoryList.get(position);
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


                convertView = inflater.inflate(R.layout.row_category_items, null);

                holder.ll_click = (LinearLayout) convertView.findViewById(R.id.ll_click);

                //NUMBER
                holder.tv_audio_number = (TextView) convertView.findViewById(R.id.tv_audio_number);
                holder.tv_audio_number.setTypeface(FontFamily.setHelveticaTypeface(parent));

                //IMAGE_VIEW AND PROGRESS BAR
                holder.iv_list = (ImageView) convertView.findViewById(R.id.iv_list);
                holder.pb_list = (ProgressBar) convertView.findViewById(R.id.pb_list);

                //TILE TEXT
                holder.tv_list_title = (TextView) convertView.findViewById(R.id.tv_list_title);
                holder.tv_list_title.setTypeface(FontFamily.setHelveticaTypeface(parent));

                //SUB_TITLE
                holder.tv_list_subtext = (TextView) convertView.findViewById(R.id.tv_list_subtext);
                holder.tv_list_subtext.setTypeface(FontFamily.setHelveticaTypeface(parent));


                holder.iv_list_more = (ImageView) convertView.findViewById(R.id.iv_list_more);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();


            final TrackAudioModel mData = categoryList.get(position);

            if (position == 0)
                holder.tv_audio_number.setText("1.");
            else
                holder.tv_audio_number.setText(position + 1 + ".");


            holder.tv_list_title.setText(mData.getTitle());

            UILoader.UILCateListLoading(holder.iv_list, mData.getThumbnail_image(), null, R.drawable.icon_list_holder);


            /*PLAY THE AUDIO*/
            holder.ll_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.pause_button_position = position;

                    parent.player.initPlaylist(categoryList, null);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TrackAudioModel mData = categoryList.get(position);
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
            ImageView iv_list_more;

            ProgressBar pb_list;

            TextView tv_list_title;
            TextView tv_list_subtext;
            TextView tv_audio_number;

            LinearLayout ll_click;
        }

        void updateAdapter(ArrayList<TrackAudioModel> results) {
            categoryList = results;
            listView.setAdapter(null);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }
}
