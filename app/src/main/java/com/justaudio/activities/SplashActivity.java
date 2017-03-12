package com.justaudio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.justaudio.R;
import com.justaudio.utils.AppConstants;
import com.justaudio.utils.ToolbarUtils;

public class SplashActivity extends BaseActivity {


    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ToolbarUtils.setTranslateStatusBar(this);


        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToLoginScreen();
            }
        }, AppConstants.SPLASH_DELAY_TIME);
    }


    /*
    * NAVIGATE TO NEXT SCREEN
    * */
    private void navigateToLoginScreen() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*REMOVE THE HANDLER*/
        handler.removeCallbacksAndMessages(null);
    }
}
