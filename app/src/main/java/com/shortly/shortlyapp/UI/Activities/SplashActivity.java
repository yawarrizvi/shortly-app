package com.shortly.shortlyapp.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.UI.Activities.Login.LoginActivity;

/**
 * Created by yarizvi on 05/06/2017.
 */

public class SplashActivity extends Activity {
    private static final int SPLASH_TIME_OUT = 2000;
    private static boolean mIsBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mIsBackPressed = false;

        /*final int mAppServerVersion =  Prefs.getInstance(this).getAppServerVersion(0);
        final int mIsUpdateAvailable =  Utilities.updateRequired(mAppServerVersion);
        final int mForcedUpdateRequired =  Prefs.getInstance(this).getIsForcedUpdateRequired(0);*/

        /**
         * Fix for Jira Bug: https://getfieldforce.atlassian.net/browse/FFMVPS-2009
         * Solution:http://stackoverflow.com/questions/16283079/re-launch-of-activity-on-home-button-but-only-the-first-time/16447508#16447508
         *
         * https://code.google.com/p/android/issues/detail?id=2373
         * https://code.google.com/p/android/issues/detail?id=26658
         */
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
                        //Intent intent = new Intent(SplashActivity.this, ShortlyTabViewActivity.class);
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
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
