package com.justaudio.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.justaudio.adapter.FavoritesAdapter;
import com.justaudio.adapter.MovieListAdapter;
import com.justaudio.dto.MovieListModel;
import com.justaudio.dto.TrackAudioModel;
import com.justaudio.interfaces.IUpdateUi;
import com.justaudio.services.ApiConfiguration;
import com.justaudio.services.JSONArrayTask;
import com.justaudio.services.JSONResult;
import com.justaudio.services.NetworkUtils;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VIDYA
 */
public class FavoritesFragment extends Fragment implements JSONResult, IUpdateUi {

    public static final String TAG = "FavoritesFragment";

    private View view;
    private HomeActivity parent;
    private JSONArrayTask getMoviesTask;

    public TextView tv_title;
    public ImageView iv_toggle;

    private ListView lv_favorite;
    private SwipeRefreshLayout swipe_container;
    private LinearLayout ll_no_data;

    private String title;
    public int pause_button_position = -1;
    private FavoritesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (HomeActivity) getActivity();

        title = getArguments().getString(AppConstants.INTENT_KEY_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        initializeTheViews();
        return view;
    }

    private void initializeTheViews() {

        /*TEXT_VIEW TITLE*/
        tv_title = (TextView) view.findViewById(R.id.tv_toolbar_title);
        tv_title.setTypeface(FontFamily.setHelveticaTypeface(parent), Typeface.BOLD);
        tv_title.setText("" + title);


        /*IMAGE_VIEW  TOGGLE*/
        iv_toggle = (ImageView) view.findViewById(R.id.iv_toggle);
        iv_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = parent.drawer_layout.isDrawerOpen(GravityCompat.START);
                if (status) {
                    parent.drawer_layout.closeDrawers();
                } else {
                    parent.drawer_layout.openDrawer(GravityCompat.START);
                }
            }
        });

         /*IMAGE VIEW SEARCH*/
        ImageView iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateSearchFragment();
            }
        });

        swipe_container = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipe_container.setColorSchemeResources(android.R.color.holo_red_light, android.
                R.color.holo_blue_light, android.R.color.holo_purple);
        swipe_container.setOnRefreshListener(swipeRefreshListener);


        lv_favorite = (ListView) view.findViewById(R.id.lv_favorite);
        lv_favorite.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                swipe_container.setEnabled(false);
            }
        });


        ll_no_data = ToolbarUtils.initializeNoDataView(parent, view, "Favorites list is empty");


        swipe_container.post(new Runnable() {
            @Override
            public void run() {
                swipe_container.setRefreshing(true);
                swipeRefreshListener.onRefresh();
            }
        });
    }

    /*SWIPE TO REFRESH LISTENER*/
    SwipeRefreshLayout.OnRefreshListener swipeRefreshListener =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (NetworkUtils.isNetworkAvailable(parent)) {
                                getMoviesData();
                            } else
                                NetworkUtils.showNetworkConnectDialog(parent, true);
                        }
                    }, 2000);
                }
            };


    /*
    * GET THE DATA FORM THE SERVER
    * */
    public void getMoviesData() {

        if (getMoviesTask != null)
            getMoviesTask.cancel(true);

        String deviceId = Utils.getDeviceID(parent);
        String url = String.format(Utils.getServer(parent, R.string.REST_ADD_TO_FAVORITES), deviceId);

        getMoviesTask = new JSONArrayTask(this);
        getMoviesTask.setMethod(JSONArrayTask.METHOD.GET);
        getMoviesTask.setCode(ApiConfiguration.REST_GET_FEV_LIST_CODE);
        getMoviesTask.setServerUrl(url);
        getMoviesTask.setErrorMessage(ApiConfiguration.ERROR_RESPONSE_CODE);
        getMoviesTask.setConnectTimeout(8000);
        getMoviesTask.execute();

    }

    @Override
    public void successJsonResult(int code, Object result) {
        if (code == ApiConfiguration.REST_GET_FEV_LIST_CODE) {
            try {
                ArrayList<TrackAudioModel> mList = new ArrayList<>();
                JSONArray array = (JSONArray) result;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jObj = array.getJSONObject(i);
                    TrackAudioModel dataItem = new TrackAudioModel(jObj);
                    mList.add(dataItem);
                }
                setData(mList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        swipe_container.setRefreshing(false);
    }

    @Override
    public void failedJsonResult(int code) {
        if (code == ApiConfiguration.REST_GET_FEV_LIST_CODE) {
            ll_no_data.setVisibility(View.VISIBLE);
            lv_favorite.setVisibility(View.GONE);
        }
        swipe_container.setRefreshing(false);

    }

    private void setData(ArrayList<TrackAudioModel> data) {
        if (data.size() > 0) {
            ll_no_data.setVisibility(View.GONE);
            lv_favorite.setVisibility(View.VISIBLE);
            adapter = new FavoritesAdapter(parent, data, FavoritesFragment.this);
            lv_favorite.setAdapter(adapter);
        } else {
            ll_no_data.setVisibility(View.VISIBLE);
            lv_favorite.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateNextUI() {

        List<TrackAudioModel> mList = parent.player.getMyPlaylist();
        if (mList != null && mList.size() != 1) {

            if (pause_button_position == mList.size() - 1)
                pause_button_position = 0;
            else
                pause_button_position = pause_button_position + 1;

            if (adapter != null)
                adapter.notifyDataSetChanged();
        }

    }
    private void navigateSearchFragment() {
        Bundle bundle = new Bundle();
        Utils.navigateFragment(new SearchFragment(), SearchFragment.TAG, bundle, parent);
    }

    @Override
    public void updatePreUI() {

        List<TrackAudioModel> mList = parent.player.getMyPlaylist();
        if (mList != null && mList.size() != 1) {
            if (pause_button_position == 0)
                pause_button_position = parent.player.getMyPlaylist().size() - 1;
            else
                pause_button_position = pause_button_position - 1;

            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateCurrentUI() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}
