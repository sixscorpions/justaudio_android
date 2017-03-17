package com.justaudio.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.justaudio.adapter.ViewPagerAdapter;
import com.justaudio.audioplayer.AudioPlayerView;
import com.justaudio.dto.MovieInfoModel;
import com.justaudio.dto.TabListModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.services.ApiConfiguration;
import com.justaudio.services.JSONResult;
import com.justaudio.services.JSONTask;
import com.justaudio.utils.CustomDialog;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.UILoader;
import com.justaudio.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by VIDYA
 */
public class PlayerFragment extends Fragment implements JSONResult {

    public static final String TAG = "PlayerFragment";

    private View view;
    private HomeActivity parent;
    private JSONTask jsonDetailTask;


    private LinearLayout ll_audio_info;
    private LinearLayout ll_main;

    private ImageView iv_parallax;
    private ImageView iv_movie_info;
    private ImageView iv_play_full_movie;

    private TextView tv_movie_name;
    private TextView tv_info_genre_value;
    private TextView tv_info_director_value;
    private TextView tv_info_language_value;
    private TextView tv_info_cast_value;
    private TextView tv_info_music_value;
    private TextView tv_info_synopsis_value;
    private TextView tv_info_production_value;


    public static ArrayList<PlayerListFragment> fragmentArrayList;
    private String movieId;
    private int position;

    public static TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (HomeActivity) getActivity();

        fragmentArrayList = new ArrayList<>();
        movieId = getArguments().getString("movie_id");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_player, container, false);
        initializeTheViews();
        return view;
    }


    private void initializeTheViews() {

        ll_main = (LinearLayout) view.findViewById(R.id.ll_main);


        ImageView iv_action_back = (ImageView) view.findViewById(R.id.iv_action_back);
        iv_action_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.onBackPressed();
            }
        });


        /*BACKGROUND IMAGE_VIEW*/
        iv_parallax = (ImageView) view.findViewById(R.id.iv_parallax);

        /*TITLE MOVIE*/
        tv_movie_name = (TextView) view.findViewById(R.id.tv_movie_name);
        tv_movie_name.setTypeface(FontFamily.setHelveticaTypeface(parent), Typeface.BOLD);


        /*INFO BUTTON*/
        iv_movie_info = (ImageView) view.findViewById(R.id.iv_movie_info);
        iv_movie_info.setVisibility(View.GONE);

        /*FULL MOVIE BUTTON*/
        iv_play_full_movie = (ImageView) view.findViewById(R.id.iv_play_full_movie);


        /*INFO LAYOUT*/
        ll_audio_info = (LinearLayout) view.findViewById(R.id.ll_audio_info);

        /*CLOSE AUDIO INFO*/
        ImageView iv_info_close = (ImageView) view.findViewById(R.id.iv_info_close);
        iv_info_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_audio_info.setVisibility(View.GONE);
            }
        });

        /*TEXT_VIEW INFO HEADER*/
        TextView tv_info_header = (TextView) view.findViewById(R.id.tv_info_header);
        tv_info_header.setTypeface(FontFamily.setHelveticaTypeface(parent), Typeface.BOLD);

        /*GENRE*/
        TextView tv_info_genre = (TextView) view.findViewById(R.id.tv_info_genre);
        tv_info_genre.setTypeface(FontFamily.setHelveticaTypeface(parent));
        tv_info_genre_value = (TextView) view.findViewById(R.id.tv_info_genre_value);
        tv_info_genre_value.setTypeface(FontFamily.setHelveticaTypeface(parent));

        /*DIRECTOR*/
        TextView tv_info_director = (TextView) view.findViewById(R.id.tv_info_director);
        tv_info_director.setTypeface(FontFamily.setHelveticaTypeface(parent));
        tv_info_director_value = (TextView) view.findViewById(R.id.tv_info_director_value);
        tv_info_director_value.setTypeface(FontFamily.setHelveticaTypeface(parent));


        /*LANGUAGE*/
        TextView tv_info_language = (TextView) view.findViewById(R.id.tv_info_language);
        tv_info_language.setTypeface(FontFamily.setHelveticaTypeface(parent));
        tv_info_language_value = (TextView) view.findViewById(R.id.tv_info_language_value);
        tv_info_language_value.setTypeface(FontFamily.setHelveticaTypeface(parent));


        /*MUSIC*/
        TextView tv_info_music = (TextView) view.findViewById(R.id.tv_info_music);
        tv_info_music.setTypeface(FontFamily.setHelveticaTypeface(parent));
        tv_info_music_value = (TextView) view.findViewById(R.id.tv_info_music_value);
        tv_info_music_value.setTypeface(FontFamily.setHelveticaTypeface(parent));


        /*PRODUCTION*/
        TextView tv_info_producation = (TextView) view.findViewById(R.id.tv_info_producation);
        tv_info_producation.setTypeface(FontFamily.setHelveticaTypeface(parent));
        tv_info_production_value = (TextView) view.findViewById(R.id.tv_info_producation_value);
        tv_info_production_value.setTypeface(FontFamily.setHelveticaTypeface(parent));


        /*CAST*/
        TextView tv_info_cast = (TextView) view.findViewById(R.id.tv_info_cast);
        tv_info_cast.setTypeface(FontFamily.setHelveticaTypeface(parent));
        tv_info_cast_value = (TextView) view.findViewById(R.id.tv_info_cast_value);
        tv_info_cast_value.setTypeface(FontFamily.setHelveticaTypeface(parent));


        /*SYNOPSIS*/
        TextView tv_info_synopsis = (TextView) view.findViewById(R.id.tv_info_synopsis);
        tv_info_synopsis.setTypeface(FontFamily.setHelveticaTypeface(parent));
        tv_info_synopsis_value = (TextView) view.findViewById(R.id.tv_info_synopsis_value);
        tv_info_synopsis_value.setTypeface(FontFamily.setHelveticaTypeface(parent));

        getAudioInfoList();
    }

    /**
     * GET THE AUDIO INFO DATA
     */
    private void getAudioInfoList() {

        if (jsonDetailTask != null)
            jsonDetailTask.cancel(true);

        CustomDialog.showProgressDialog(parent, false);
        String url = String.format(Utils.getServer(parent, R.string.REST_GET_MOVIE_DETAILS_CODE), movieId);

        jsonDetailTask = new JSONTask(this);
        jsonDetailTask.setMethod(JSONTask.METHOD.GET);
        jsonDetailTask.setCode(ApiConfiguration.REST_GET_MOVIE_DETAILS_CODE);
        jsonDetailTask.setServerUrl(url);
        jsonDetailTask.setErrorMessage(ApiConfiguration.ERROR_RESPONSE_CODE);
        jsonDetailTask.setConnectTimeout(15000);
        jsonDetailTask.execute();

    }


    @Override
    public void successJsonResult(int code, Object result) {
        if (code == ApiConfiguration.REST_GET_MOVIE_DETAILS_CODE) {
            try {
                JSONObject jObj = (JSONObject) result;
                MovieInfoModel model = new MovieInfoModel(jObj);
                parent.audioModel = model;
                setInfoData(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        CustomDialog.hideProgressBar(parent);
    }


    private void setInfoData(final MovieInfoModel model) {

        ll_main.setVisibility(View.VISIBLE);

        iv_play_full_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.player.initPlaylist(model.getFullMovieList());
                final TrackAudioModel mData = model.getFullMovieList().get(0);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PlayerListFragment fragment = fragmentArrayList.get(position);
                        fragment.updateCurrentUI();
                        parent.player.playAudio(mData, fragment);
                    }
                }, 200);

            }
        });


        UILoader.UILDetailPicLoading(iv_parallax, model.getMovie_background_image(),
                R.drawable.ic_gallery_placeholder);

        tv_movie_name.setText("" + model.getMovie_name());

        /*MOVIE INFO VIEW*/
        iv_movie_info.setVisibility(View.VISIBLE);
        iv_movie_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_audio_info.setVisibility(View.VISIBLE);
            }
        });

        tv_info_genre_value.setText("" + model.getGenre());
        tv_info_director_value.setText("" + model.getDirector());
        tv_info_language_value.setText("" + model.getMovie_lang());
        tv_info_cast_value.setText("" + model.getCast());
        tv_info_synopsis_value.setText("" + model.getSynopsis());
        tv_info_production_value.setText("" + model.getProducation());
        tv_info_music_value.setText("" + model.getMusic());

        initializeTheTabs(model);
    }

    private void initializeTheTabs(MovieInfoModel model) {


        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        /*LIST VIEWPAGER*/
        ArrayList<TabListModel> tabList = model.getTabList();

        for (int i = 0; i < tabList.size(); i++) {
            String tabName = tabList.get(i).getTabName();
            Bundle bundle = new Bundle();
            bundle.putInt("Position", i);
            bundle.putString("tabName", tabName);
            PlayerListFragment fragment = new PlayerListFragment();
            adapter.addFragment(fragment, tabName, bundle);
            fragmentArrayList.add(fragment);
        }

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(0);


        tabLayout.setupWithViewPager(mViewPager);
        ToolbarUtils.setViewPageTypeface(parent, tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                AudioPlayerView.fragment = null;
                PlayerListFragment fragment = fragmentArrayList.get(position);
                //fragment.updateCurrentUI();
                ArrayList<TrackAudioModel> results = fragment.results;

                if (results != null) {
                    parent.player.initPlaylist(results);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                position = pos;
                Fragment fragment = fragmentArrayList.get(position);
                ArrayList<TrackAudioModel> results = ((PlayerListFragment) fragment).results;
                if (results != null) {
                    parent.player.initPlaylist(results);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void failedJsonResult(int code) {
        CustomDialog.hideProgressBar(parent);
    }
}
