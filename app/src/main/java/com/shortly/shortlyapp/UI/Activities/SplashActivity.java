package com.shortly.shortlyapp.UI.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.UI.Activities.Login.LoginActivity;

/**
 * Created by yarizvi on 05/06/2017.
 */

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_TIME_OUT = 2000;
    private static boolean mIsBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mIsBackPressed = false;

        if (!isTaskRoot()) {
            // Android launched another instance of the root activity into an existing task
            //  so just quietly finish and go away, dropping the user back into the activity
            //  at the top of the stack (ie: the last state of this task)
            finish();
            return;
        }

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_TIME_OUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (!mIsBackPressed) {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                        Intent intent = new Intent(SplashActivity.this, ShortlyTabViewActivity.class);

                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();

    }

    @Override
    public void onBackPressed() {
        mIsBackPressed = true;
        finish();
        super.onBackPressed();

    }
}