package com.justaudio.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.justaudio.adapter.ViewPagerAdapter;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.Utils;

/**
 * Created by VIDYA
 */
public class HomeNewFragment extends Fragment {

    public static final String TAG = "HomeNewFragment";

    private View view;
    private HomeActivity parent;

    public TextView tv_title;
    public ImageView iv_toggle;


    private String title;

    public ViewPager mViewPager;


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

        view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeTheViews();
        return view;
    }

    private void initializeTheViews() {

        /*TEXT_VIEW TITLE*/
        tv_title = (TextView) view.findViewById(R.id.tv_toolbar_title);
        tv_title.setTypeface(FontFamily.setHelveticaTypeface(parent), Typeface.BOLD);
        tv_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_toolbar_logo, 0, 0, 0);


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

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        Bundle bundle = new Bundle();
        adapter.addFragment(new MovieFragment(), "Movies", bundle);
        adapter.addFragment(new TvShowFragment(), "TV Shows", bundle);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(0);

        tabLayout.setupWithViewPager(mViewPager);
        ToolbarUtils.setViewPageTypeface(parent, tabLayout);

    }

    private void navigateSearchFragment() {
        Bundle bundle = new Bundle();
        Utils.navigateFragment(new SearchFragment(), SearchFragment.TAG, bundle, parent);
    }

}
