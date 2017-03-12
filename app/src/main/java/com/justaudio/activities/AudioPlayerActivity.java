package com.justaudio.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.justaudio.R;
import com.justaudio.adapter.ViewPagerAdapter;
import com.justaudio.dto.MovieInfoModel;
import com.justaudio.dto.MovieListModel;
import com.justaudio.dto.NamesModelNew;
import com.justaudio.dto.TabListModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.audioplayer.PlayerService;
import com.justaudio.audioplayer.AudioPlayerView;
import com.justaudio.fragment.PlayerListFragment;
import com.justaudio.services.ApiConfiguration;
import com.justaudio.services.JSONArrayTask;
import com.justaudio.services.JSONResult;
import com.justaudio.services.JSONTask;
import com.justaudio.services.NetworkUtils;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.CustomDialog;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.UILoader;
import com.justaudio.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AudioPlayerActivity extends BaseActivity implements JSONResult, PlayerService.OnInvalidPathListener {


    private JSONArrayTask jsonTask;
    private JSONTask jsonDetailTask;

    public AudioPlayerView player;
    private ArrayList<android.support.v4.app.Fragment> fragmentArrayList;
    private int position;

    private MovieListModel movieListModel;
    private LinearLayout ll_audio_info;


    private TextView tv_info_genre_value;
    private TextView tv_info_director_value;
    private TextView tv_info_language_value;
    private TextView tv_info_cast_value;
    private TextView tv_info_music_value;
    private TextView tv_info_synopsis_value;
    private TextView tv_info_producation_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        ToolbarUtils.setAudioPlayerToolbar(this);
        fragmentArrayList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        movieListModel = (MovieListModel) bundle.getSerializable(AppConstants.INTENT_KEY_MOVIE_ID);

        initilizeAudioInfoView();
        getAudioInfoList();
    }

    private void initilizeAudioInfoView() {

        /*CLOSE AUDIO INFO*/
        ImageView iv_info_close = (ImageView) findViewById(R.id.iv_info_close);
        iv_info_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_audio_info.setVisibility(View.GONE);
            }
        });

        /*TEXT_VIEW INFO HEADER*/
        TextView tv_info_header = (TextView) findViewById(R.id.tv_info_header);
        tv_info_header.setTypeface(FontFamily.setHelveticaTypeface(this), Typeface.BOLD);

        /*GENRE*/
        TextView tv_info_genre = (TextView) findViewById(R.id.tv_info_genre);
        tv_info_genre.setTypeface(FontFamily.setHelveticaTypeface(this));
        tv_info_genre_value = (TextView) findViewById(R.id.tv_info_genre_value);
        tv_info_genre_value.setTypeface(FontFamily.setHelveticaTypeface(this));

        /*DIRECTOR*/
        TextView tv_info_director = (TextView) findViewById(R.id.tv_info_director);
        tv_info_director.setTypeface(FontFamily.setHelveticaTypeface(this));
        tv_info_director_value = (TextView) findViewById(R.id.tv_info_director_value);
        tv_info_director_value.setTypeface(FontFamily.setHelveticaTypeface(this));


        /*LANGUAGE*/
        TextView tv_info_language = (TextView) findViewById(R.id.tv_info_language);
        tv_info_language.setTypeface(FontFamily.setHelveticaTypeface(this));
        tv_info_language_value = (TextView) findViewById(R.id.tv_info_language_value);
        tv_info_language_value.setTypeface(FontFamily.setHelveticaTypeface(this));


        /*MUSIC*/
        TextView tv_info_music = (TextView) findViewById(R.id.tv_info_music);
        tv_info_music.setTypeface(FontFamily.setHelveticaTypeface(this));
        tv_info_music_value = (TextView) findViewById(R.id.tv_info_music_value);
        tv_info_music_value.setTypeface(FontFamily.setHelveticaTypeface(this));


        /*PRODUCTION*/
        TextView tv_info_producation = (TextView) findViewById(R.id.tv_info_producation);
        tv_info_producation.setTypeface(FontFamily.setHelveticaTypeface(this));
        tv_info_producation_value = (TextView) findViewById(R.id.tv_info_producation_value);
        tv_info_producation_value.setTypeface(FontFamily.setHelveticaTypeface(this));


        /*CAST*/
        TextView tv_info_cast = (TextView) findViewById(R.id.tv_info_cast);
        tv_info_cast.setTypeface(FontFamily.setHelveticaTypeface(this));
        tv_info_cast_value = (TextView) findViewById(R.id.tv_info_cast_value);
        tv_info_cast_value.setTypeface(FontFamily.setHelveticaTypeface(this));


        /*SYNOPSIS*/
        TextView tv_info_synopsis = (TextView) findViewById(R.id.tv_info_synopsis);
        tv_info_synopsis.setTypeface(FontFamily.setHelveticaTypeface(this));
        tv_info_synopsis_value = (TextView) findViewById(R.id.tv_info_synopsis_value);
        tv_info_synopsis_value.setTypeface(FontFamily.setHelveticaTypeface(this));


    }

    /*
  * GET THE DATA FORM THE SERVER
  * */
    private void getMoviesAudioList() {

        if (jsonTask != null)
            jsonTask.cancel(true);

        CustomDialog.showProgressDialog(this, false);
        String url = String.format(Utils.getServer(this, R.string.REST_GET_MOVIE_AUDIOS_LIST), movieListModel.getMovie_id());

        jsonTask = new JSONArrayTask(this);
        jsonTask.setMethod(JSONArrayTask.METHOD.GET);
        jsonTask.setCode(ApiConfiguration.REST_GET_MOVIE_AUDIOS_LIST_CODE);
        jsonTask.setServerUrl(url);
        jsonTask.setErrorMessage(ApiConfiguration.ERROR_RESPONSE_CODE);
        jsonTask.setConnectTimeout(8000);
        jsonTask.execute();

    }


    /**
     * GET THE AUDIO INFO DATA
     */
    private void getAudioInfoList() {

        if (jsonDetailTask != null)
            jsonDetailTask.cancel(true);

        CustomDialog.showProgressDialog(this, false);
        String url = String.format(Utils.getServer(this, R.string.REST_GET_MOVIE_DETAILS_CODE), movieListModel.getMovie_id());

        jsonDetailTask = new JSONTask(this);
        jsonDetailTask.setMethod(JSONTask.METHOD.GET);
        jsonDetailTask.setCode(ApiConfiguration.REST_GET_MOVIE_DETAILS_CODE);
        jsonDetailTask.setServerUrl(url);
        jsonDetailTask.setErrorMessage(ApiConfiguration.ERROR_RESPONSE_CODE);
        jsonDetailTask.setConnectTimeout(8000);
        jsonDetailTask.execute();

    }

    @Override

    public void successJsonResult(int code, Object result) {
        /*if (code == ApiConfiguration.REST_GET_MOVIE_AUDIOS_LIST_CODE) {
            try {
                JSONArray array = (JSONArray) result;
                JSONObject jObj = array.getJSONObject(0);

                TrackAudioModel model = new TrackAudioModel(jObj);
                initializeTheViews(model);

            } catch (JSONException e) {
                e.printStackTrace();
                NetworkUtils.showAlertDialog(this, jsonTask.getResultMessage());
            }
        }*/

        if (code == ApiConfiguration.REST_GET_MOVIE_DETAILS_CODE) {
            try {
                JSONObject jObj = (JSONObject) result;
                MovieInfoModel model = new MovieInfoModel(jObj);
                setMovieInfoData(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        CustomDialog.hideProgressBar(this);
    }

    @Override
    public void failedJsonResult(int code) {
        if (code == ApiConfiguration.REST_GET_MOVIE_AUDIOS_LIST_CODE)
            NetworkUtils.showAlertDialog(this, jsonTask.getResultMessage());
        else if (code == ApiConfiguration.REST_GET_MOVIE_DETAILS_CODE)

            CustomDialog.hideProgressBar(this);
    }


    private void setMovieInfoData(MovieInfoModel model) {

        player = (AudioPlayerView) findViewById(R.id.jcplayer);
        player.registerInvalidPathListener(this);

        /*BACKGROUND IMAGE_VIEW*/
        ImageView iv_parallax = (ImageView) findViewById(R.id.iv_parallax);
        UILoader.UILDetailPicLoading(iv_parallax, movieListModel.getMovie_background_image(),
                R.drawable.ic_gallery_placeholder);

        /*TEXT_VIEW MOVIE NAME*/
        TextView tv_movie_name = (TextView) findViewById(R.id.tv_movie_name);
        tv_movie_name.setTypeface(FontFamily.setHelveticaTypeface(this), Typeface.BOLD);
        tv_movie_name.setText(movieListModel.getMovie_name());



        /*INFO LAYOUT*/
        ll_audio_info = (LinearLayout) findViewById(R.id.ll_audio_info);

        /*INFO BUTTON*/
        ImageView iv_movie_info = (ImageView) findViewById(R.id.iv_movie_info);
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
        tv_info_producation_value.setText("" + model.getProducation());
        tv_info_music_value.setText("" + model.getMusic());

        initializeTheTabs(model);
    }

    private void initializeTheTabs(MovieInfoModel model) {

        /*LIST VIEWPAGER*/
        ArrayList<TabListModel> tabList = model.getTabList();
        if (tabList.size() > 0) {
            ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            for (int i = 0; i < tabList.size(); i++) {
                String tabName = tabList.get(i).getTabName();
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.INTENT_KEY_OBJECT_MOVIE, model);
                bundle.putInt("Position", i);
                bundle.putString("tabName", tabName);
                Fragment fragment = new PlayerListFragment();
                adapter.addFragment(fragment, tabName, bundle);
                fragmentArrayList.add(fragment);
            }
            mViewPager.setAdapter(adapter);
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setCurrentItem(0);
            tabLayout.setupWithViewPager(mViewPager);
            ToolbarUtils.setViewPageTypeface(this, tabLayout);


            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    position = tab.getPosition();
                    Fragment fragment = fragmentArrayList.get(position);
                    ArrayList<TrackAudioModel> results = ((PlayerListFragment) fragment).results;

                    if (results != null) {
                        if (player != null)
                            player.initPlaylist(results);
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
                        if (player != null)
                            player.initPlaylist(results);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }




   /* public void playAudio(TrackAudioModel trackAudioModel) {
        player.playAudio(trackAudioModel);
        //Toast.makeText(this, player.getCurrentAudio().getOrigin().toString(), Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onPause() {
        super.onPause();
        // player.createNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.kill();
    }

    @Override
    public void onPathError(TrackAudioModel trackAudioModel) {
        Toast.makeText(this, trackAudioModel.getPath() + " with problems", Toast.LENGTH_LONG).show();
//        player.removeAudio(trackAudioModel);
//        player.next();
    }
}
