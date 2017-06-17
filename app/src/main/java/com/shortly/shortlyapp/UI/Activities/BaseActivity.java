package com.shortly.shortlyapp.UI.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.shortly.shortlyapp.DataProvider.Prefs;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        Prefs pref = Prefs.getInstance(this);
        pref.putAuthenticationToken("$2y$10$jRlduTUueXDxgSiDPo8Az.1XUMfENNDzCX1cJQjIKWLrZBpAF3LQ6");

    }

}
