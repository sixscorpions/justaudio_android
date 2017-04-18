package com.justaudio.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
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
public class SearchFragment extends Fragment implements JSONResult {

    public static final String TAG = "SearchFragment";

    private View view;
    private HomeActivity parent;
    private JSONArrayTask getMoviesTask;


    private GridView gv_movies;
    private LinearLayout ll_no_data;

    private EditText et_search;
    private ImageView iv_search_cancel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (HomeActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_search, container, false);
        initializeTheViews();
        return view;
    }

    private void initializeTheViews() {


        /*IMAGE_VIEW  TOGGLE*/
        ImageView iv_toggle = (ImageView) view.findViewById(R.id.iv_toggle);
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


        /*EDIT TEXT*/
        et_search = (EditText) view.findViewById(R.id.et_search);
        et_search.setTypeface(FontFamily.setHelveticaTypeface(parent));
        et_search.addTextChangedListener(movieWatcher);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchTag = et_search.getText().toString();
                    getMoviesData(searchTag);
                    return true;
                }
                return false;
            }

        });
        Utils.showKeyboard(parent);



        /*CANCEL BUTTON*/
        iv_search_cancel = (ImageView) view.findViewById(R.id.iv_search_cancel);
        iv_search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getMoviesTask != null)
                    getMoviesTask.cancel(true);
                et_search.setText("");
            }
        });


        gv_movies = (GridView) view.findViewById(R.id.gv_movies);


        ll_no_data = ToolbarUtils.initializeNoDataView(parent, view, "");

    }


    private TextWatcher movieWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String searchTag = et_search.getText().toString();

            if (searchTag.length() > 0)
                iv_search_cancel.setVisibility(View.VISIBLE);
            else
                iv_search_cancel.setVisibility(View.GONE);


            if (!Utils.isValueNullOrEmpty(searchTag) && searchTag.length() > 2)
                getMoviesData(searchTag);

        }
    };


    /*
    * GET THE DATA FORM THE SERVER
    * */
    public void getMoviesData(String searchTag) {

        if (getMoviesTask != null)
            getMoviesTask.cancel(true);


        String url = String.format(Utils.getServer(parent, R.string.REST_GET_MOVIES_SEARCH), searchTag);

        getMoviesTask = new JSONArrayTask(this);
        getMoviesTask.setMethod(JSONArrayTask.METHOD.GET);
        getMoviesTask.setCode(ApiConfiguration.REST_GET_MOVIE_SEARCH);
        getMoviesTask.setServerUrl(url);
        getMoviesTask.setErrorMessage(ApiConfiguration.ERROR_RESPONSE_CODE);
        getMoviesTask.setConnectTimeout(8000);
        getMoviesTask.execute();

    }

    @Override
    public void successJsonResult(int code, Object result) {
        if (code == ApiConfiguration.REST_GET_MOVIE_SEARCH) {
            try {
                ArrayList<MovieListModel> mList = new ArrayList<>();

                JSONArray array = (JSONArray) result;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jObj = array.getJSONObject(i);
                    MovieListModel dataItem = new MovieListModel(jObj);
                    mList.add(dataItem);
                }
                setData(mList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setData(ArrayList<MovieListModel> data) {

        if (data.size() > 0) {
            ll_no_data.setVisibility(View.GONE);
            gv_movies.setVisibility(View.VISIBLE);
            MovieListAdapter adapter = new MovieListAdapter(parent, data);
            gv_movies.setAdapter(adapter);
        } else {
            ll_no_data.setVisibility(View.VISIBLE);
            gv_movies.setVisibility(View.GONE);
        }
        gv_movies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Utils.hideSoftKeyPad(parent);
                MovieListModel model = (MovieListModel) adapterView.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("movie_id", model.getMovie_id());
                Utils.navigateFragment(new PlayerFragment(), PlayerFragment.TAG, bundle, parent);
            }
        });
    }


    @Override
    public void failedJsonResult(int code) {
        if (code == ApiConfiguration.REST_GET_MOVIE_SEARCH) {
            ll_no_data.setVisibility(View.VISIBLE);
            gv_movies.setVisibility(View.GONE);
        }

    }

}
