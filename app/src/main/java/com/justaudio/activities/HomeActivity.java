package com.justaudio.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.adapter.LeftMenuAdapter;
import com.justaudio.dto.LeftMenuModel;
import com.justaudio.fragment.HomeFragment;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.ToolbarUtils;
import com.justaudio.utils.Utils;


public class HomeActivity extends BaseActivity {

    public DrawerLayout drawer_layout;
    public TextView tv_title;
    public ImageView iv_toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_layout.setFocusable(true);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        /*TEXT_VIEW TITLE*/
        tv_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_title.setTypeface(FontFamily.setHelveticaTypeface(this), Typeface.BOLD);

        /*TEXT VERSION NUMBER*/
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setTypeface(FontFamily.setHelveticaTypeface(this), Typeface.BOLD);



        /*IMAGE_VIEW  TOGGLE*/
        iv_toggle = (ImageView) findViewById(R.id.iv_toggle);
        iv_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = drawer_layout.isDrawerOpen(GravityCompat.START);
                if (status) {
                    drawer_layout.closeDrawers();
                } else {
                    drawer_layout.openDrawer(GravityCompat.START);
                }
            }
        });
        setLeftMenuData();
    }


    private void setLeftMenuData() {
        /*LIST VIEW*/
        ListView lv_left_menu = (ListView) findViewById(R.id.lv_left_menu);


        final LeftMenuAdapter leftMenuAdapter = new LeftMenuAdapter(this, ToolbarUtils.getLeftMenuList());
        lv_left_menu.setAdapter(leftMenuAdapter);
        lv_left_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawer_layout.closeDrawers();
                LeftMenuModel item = (LeftMenuModel) parent.getAdapter().getItem(position);
                /*ASSIGN TITLE TO THE TOOLBAR*/
                String title = item.getMenuTitle();
                tv_title.setText(title);

                switch (title) {
                    case "Home":
                        navigateHomeFragment();
                        break;
                }

            }
        });
        lv_left_menu.performItemClick(lv_left_menu, 0, 0);
    }

    private void navigateHomeFragment() {
        Bundle bundle = new Bundle();
        Utils.navigateFragment(new HomeFragment(), HomeFragment.TAG, bundle, this);
    }

    @Override
    public void onBackPressed() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry backStackEntry
                    = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
           /* if (backStackEntry.getName().equalsIgnoreCase(HomeFragment.TAG)) {
                super.onBackPressed();
            } else*/
                finish();
        } else
            finish();
    }
}
