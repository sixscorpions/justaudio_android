package com.justaudio.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.justaudio.adapter.MovieListAdapter;
import com.justaudio.dto.MovieListModel;
import com.justaudio.services.ApiConfiguration;
import com.justaudio.services.JSONResult;
import com.justaudio.services.JSONTask;
import com.justaudio.services.NetworkUtils;
import com.justaudio.utils.CustomDialog;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by VIDYA
 */
public class MovieFragment extends Fragment implements JSONResult,
        AbsListView.OnScrollListener {


    private View view;
    private HomeActivity parent;
    private JSONTask getMoviesTask;


    private GridView gv_movies;
    private LinearLayout ll_no_data;

    private int index = 0;
    private int aaTotalCount;
    private int aaVisibleCount;
    private int aaFirstVisibleItem;
    private boolean endScroll = false;


    private ArrayList<MovieListModel> movieList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (HomeActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_movie_tv_list, container, false);
        initializeTheViews();
        return view;
    }

    private void initializeTheViews() {


        gv_movies = (GridView) view.findViewById(R.id.gv_movies);
        gv_movies.setOnScrollListener(this);


        ll_no_data = ToolbarUtils.initializeNoDataView(parent, view, "");

        if (NetworkUtils.isNetworkAvailable(parent)) {
            movieList = null;
            getMoviesData();
        } else
            NetworkUtils.showNetworkConnectDialog(parent, true);

    }


    /*
    * GET THE DATA FORM THE SERVER
    * */
    private void getMoviesData() {

        if (getMoviesTask != null)
            getMoviesTask.cancel(true);

        String url = Utils.getServer(parent, R.string.REST_GET_MOVIES_LIST_PAGINATION) +
                "?page=" + index + "&size=" + ApiConfiguration.LIST_COUNT;

        CustomDialog.showProgressDialog(parent, false);

        getMoviesTask = new JSONTask(this);
        getMoviesTask.setMethod(JSONTask.METHOD.GET);
        getMoviesTask.setCode(ApiConfiguration.REST_GET_MOVIES_LIST_CODE);
        getMoviesTask.setServerUrl(url);
        getMoviesTask.setErrorMessage(ApiConfiguration.ERROR_RESPONSE_CODE);
        getMoviesTask.setConnectTimeout(15000);
        getMoviesTask.execute();

    }

    @Override
    public void successJsonResult(int code, Object result) {
        if (code == ApiConfiguration.REST_GET_MOVIES_LIST_CODE) {

            try {
                JSONObject object = (JSONObject) result;
                JSONArray array = object.getJSONArray("content");
                if (movieList == null)
                    movieList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jObj = array.getJSONObject(i);
                    MovieListModel dataItem = new MovieListModel(jObj);
                    movieList.add(dataItem);
                }

                /*UPDATE THE INDEX*/
                int totalPages = Integer.parseInt(object.getString("totalPages"));
                if (totalPages == index) {
                    endScroll = true;
                } else {
                    endScroll = false;
                    index++;
                }
                setData(movieList);

            } catch (JSONException e) {
                e.printStackTrace();
                NetworkUtils.showAlertDialog(parent, getMoviesTask.getResultMessage());
            }
        }
        CustomDialog.hideProgressBar(parent);
    }

    @Override
    public void failedJsonResult(int code) {
        if (code == ApiConfiguration.REST_GET_MOVIES_LIST_CODE)
            NetworkUtils.showAlertDialog(parent, getMoviesTask.getResultMessage());

        CustomDialog.hideProgressBar(parent);
    }

    MovieListAdapter adapter;

    private void setData(ArrayList<MovieListModel> data) {

        if (data.size() > 0) {
            ll_no_data.setVisibility(View.GONE);
            gv_movies.setVisibility(View.VISIBLE);

            if (adapter == null) {
                adapter = new MovieListAdapter(parent, data);
                gv_movies.setAdapter(adapter);
            } else
                adapter.updateAdapter(data);

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


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            isScrollCompleted();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        aaTotalCount = totalItemCount;
        aaVisibleCount = visibleItemCount;
        aaFirstVisibleItem = firstVisibleItem;
       // int last_item = firstVisibleItem + visibleItemCount - 1;
    }

    private void isScrollCompleted() {
        if (aaTotalCount == (aaFirstVisibleItem + aaVisibleCount) && !endScroll) {
            getMoviesData();
        }
    }
}
