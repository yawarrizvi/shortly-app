package com.shortly.shortlyapp.UI.Activities;

import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by yarizvi on 10/06/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

}
