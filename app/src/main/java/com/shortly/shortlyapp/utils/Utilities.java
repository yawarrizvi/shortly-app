package com.shortly.shortlyapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.shortly.shortlyapp.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by yarizvi on 10/06/2017.
 */

public class Utilities {

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Activity activity, View view) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static String getMovieDurationString(Context context, long duration) {
        String durationString = "";


        int day = (int) TimeUnit.SECONDS.toDays(duration);
        long hours = TimeUnit.SECONDS.toHours(duration) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(duration) - (TimeUnit.SECONDS.toHours(duration) * 60);
        long second = TimeUnit.SECONDS.toSeconds(duration) - (TimeUnit.SECONDS.toMinutes(duration) * 60);
        if (day > 0) {
            String dayString = day > 1 ? context.getString(R.string.key_string_days) : context.getString(R.string.key_string_day);
            durationString = day + " " + dayString;
        }

        if (hours > 0) {
            String hourString = hours > 1 ? context.getString(R.string.key_string_hrs) : context.getString(R.string.key_string_hr);
            durationString += " " + hours + " " + hourString;
        }

        if (minute > 0) {
            String minuteString = minute > 1 ? context.getString(R.string.key_string_mins) : context.getString(R.string.key_string_min);
            if (!durationString.isEmpty() && day > 0) {
                durationString += " ";
            } else {
                durationString += " ";
            }
            durationString += minute + " " + minuteString;
        }

        if (durationString.isEmpty()) {
            if (second == 0) {
                second = 1;
            }
            durationString = second + " " + context.getString(R.string.key_string_second);
        }

        return durationString;
    }
}
