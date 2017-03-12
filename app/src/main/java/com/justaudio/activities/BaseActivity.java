package com.justaudio.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.justaudio.R;

/**
 * Created by Pavan
 */

public class BaseActivity extends AppCompatActivity {

    public Dialog progressDialog = null;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
