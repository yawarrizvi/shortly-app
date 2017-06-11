package com.shortly.shortlyapp.Sync;

import android.content.Context;

import com.shortly.shortlyapp.DataProvider.Prefs;
import com.shortly.shortlyapp.model.LoginResponse;

/**
 * Created by yarizvi on 08/06/2017.
 */

public class DataPersistence {


    public static void setUserDetailsInSharedPreferences(LoginResponse user, String password, Context context) {
        Prefs pref = Prefs.getInstance(context);
        pref.putAuthenticationToken(user.getApi_token());
        pref.putUserEmail(user.getEmail());
        pref.putEncryptedPassword(password);
        pref.putUserId(user.getUserId());
        pref.setCurrentUser(user);
    }
}
