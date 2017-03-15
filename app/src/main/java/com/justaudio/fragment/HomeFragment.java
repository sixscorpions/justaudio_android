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
import android.widget.TextView;

import com.justaudio.activities.HomeActivity;
import com.justaudio.adapter.MovieListAdapter;
import com.justaudio.R;
import com.justaudio.dto.MovieListModel;
import com.justaudio.services.ApiConfiguration;
import com.justaudio.services.JSONArrayTask;
import com.justaudio.services.JSONResult;
import com.justaudio.services.NetworkUtils;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.AudioUtils;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by VIDYA
 */
public class HomeFragment extends Fragment implements JSONResult {

    public static final String TAG = "HomeFragment";

    private View view;
    private HomeActivity parent;
    private JSONArrayTask getMoviesTask;

    public TextView tv_title;
    public ImageView iv_toggle;

    private GridView gv_movies;
    private SwipeRefreshLayout swipe_container;
    private LinearLayout ll_no_data;


    private ArrayList<MovieListModel> movieList;
    private String title;


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

        view = inflater.inflate(R.layout.fragment_home_new, container, false);
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

        swipe_container = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipe_container.setColorSchemeResources(android.R.color.holo_red_light, android.
                R.color.holo_blue_light, android.R.color.holo_purple);
        swipe_container.setOnRefreshListener(swipeRefreshListener);

        gv_movies = (GridView) view.findViewById(R.id.gv_movies);
        gv_movies.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipe_container.setEnabled(true);
                else swipe_container.setEnabled(false);
            }
        });


        ll_no_data = ToolbarUtils.initializeNoDataView(parent, view);

        swipe_container.post(new Runnable() {
            @Override
            public void run() {
                swipe_container.setRefreshing(true);
                swipeRefreshListener.onRefresh();
            }
        });
    }

    /*SWIPE TO REFRESH LISTNER*/
    SwipeRefreshLayout.OnRefreshListener swipeRefreshListener =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (NetworkUtils.isNetworkAvailable(parent)) {
                                movieList = null;
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
    private void getMoviesData() {

        if (getMoviesTask != null)
            getMoviesTask.cancel(true);

        getMoviesTask = new JSONArrayTask(this);
        getMoviesTask.setMethod(JSONArrayTask.METHOD.GET);
        getMoviesTask.setCode(ApiConfiguration.REST_GET_MOVIES_CODE);
        getMoviesTask.setServerUrl(Utils.getServer(parent, R.string.REST_GET_MOVIES_CODE));
        getMoviesTask.setErrorMessage(ApiConfiguration.ERROR_RESPONSE_CODE);
        getMoviesTask.setConnectTimeout(10000);
        getMoviesTask.execute();

    }

    @Override
    public void successJsonResult(int code, Object result) {
        if (code == ApiConfiguration.REST_GET_MOVIES_CODE) {

            try {
                JSONArray array = (JSONArray) result;
                if (movieList == null)
                    movieList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jObj = array.getJSONObject(i);
                    MovieListModel dataItem = new MovieListModel(jObj);
                    movieList.add(dataItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                NetworkUtils.showAlertDialog(parent, getMoviesTask.getResultMessage());
            }
            setData(movieList);
        }
        swipe_container.setRefreshing(false);
    }

    @Override
    public void failedJsonResult(int code) {
        if (code == ApiConfiguration.REST_GET_MOVIES_CODE)
            NetworkUtils.showAlertDialog(parent, getMoviesTask.getResultMessage());
        swipe_container.setRefreshing(false);
    }

    private void setData(ArrayList<MovieListModel> data) {

        if (data.size() > 0) {
            ll_no_data.setVisibility(View.GONE);
            gv_movies.setVisibility(View.VISIBLE);

            MovieListAdapter adapter = new MovieListAdapter(parent, data);
            gv_movies.setAdapter(adapter);
            gv_movies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    MovieListModel model = (MovieListModel) adapterView.getAdapter().getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("movie_id", model.getMovie_id());
                    Utils.navigateFragment(new PlayerFragment(), PlayerFragment.TAG, bundle, parent);
                }
            });
        } else {
            ll_no_data.setVisibility(View.VISIBLE);
            gv_movies.setVisibility(View.GONE);
        }
    }
}
