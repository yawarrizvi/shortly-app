package com.shortly.shortlyapp.DataProvider;

import android.content.Context;

import com.google.gson.Gson;
import com.shortly.shortlyapp.model.LoginResponse;
import com.shortly.shortlyapp.utils.PrefUtils;

/**
 * Created by yarizvi on 08/06/2017.
 */

public class Prefs extends PrefUtils {
    private static Prefs instance;


    //Login
    public static final String KEY_AUTHENTICATION_TOKEN = "authenticationToken";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_PASSWORD = "userPassword";
    public static final String KEY_USER_Id = "userId";
    public static final String KEY_CURRENT_USER = "currentUser";


    private Prefs(Context context) {
        create(context);
    }


    public static Prefs getInstance(Context context) {
        if (instance == null)
            instance = new Prefs(context);

        return instance;
    }


    public void putAuthenticationToken(String authToken) {
        putString(KEY_AUTHENTICATION_TOKEN, authToken);
    }

    public String getAuthenticationToken() {
        return getString(KEY_AUTHENTICATION_TOKEN, "");
    }

    public void putUserEmail(String email) {
        putString(KEY_USER_NAME, email);
    }

    public String getEmployeeEmail() {
        return getString(KEY_USER_NAME, "");
    }

    public void putEncryptedPassword(String encryptedPassword) {
        putString(KEY_USER_PASSWORD, encryptedPassword);
    }

    public String getEmployeePassword() {
        return getString(KEY_USER_PASSWORD, "");
    }

    public void putUserId(int employeeId) {
        putInt(KEY_USER_Id, employeeId);
    }

    public int getUserId() {
        return getInt(KEY_USER_Id, 0);
    }

    public void setCurrentUser(LoginResponse user) {
        Gson gson = new Gson();
        String JSONObject = gson.toJson(user);
        putString(KEY_CURRENT_USER, JSONObject);
    }

    public LoginResponse getCurrentUser() {
        Gson gson = new Gson();
        String json = getString(KEY_CURRENT_USER, "");
        LoginResponse user = gson.fromJson(json, LoginResponse.class);
        return user;
    }

}
