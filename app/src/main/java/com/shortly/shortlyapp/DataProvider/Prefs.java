package com.shortly.shortlyapp.DataProvider;

import android.content.Context;

import com.google.gson.Gson;
import com.shortly.shortlyapp.model.LoginResponse;
import com.shortly.shortlyapp.model.LoginResponseDTO;
import com.shortly.shortlyapp.utils.PrefUtils;

/**
 * Created by yarizvi on 08/06/2017.
 */

public class Prefs extends PrefUtils {
    private static Prefs instance;
    public final String KEY_SELECTED_SURVEY_TYPE = getKey("lastSelectedSurveyType");
    public final String KEY_LAST_TASK_SEARCHED = getKey("lastTaskSearched");
    public final String KEY_GPS_ENABLED = getKey("gpsEnabled");
    public final String KEY_QUESTION_LIST_TAB_SELECTED = getKey("lastQuestionListTabSelected");
    public final String KEY_QUESTION_DETAIL_TAB_SELECTED = getKey("lastQuestionDetailTabSelected");
    public final String KEY_QUESTION_DETAIL_Question_SELECTED = getKey("lastQuestionDetailQueSelected");

    public final String KEY_FORCED_UPDATE_REQUIRED = getKey("forcedUpdateRequired");
    public final String KEY_UPDATE_AVAILABLE = getKey("updateAvailable");
    public final String KEY_APP_SERVER_VERSION = getKey("appServerVersion");
    public final String KEY_SYNC_UNSUCCESSFUL = getKey("gpsEnabled");


    //Login
    public static final String KEY_AUTHENTICATION_TOKEN = "authenticationToken";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_PASSWORD = "userPassword";
    public static final String KEY_USER_Id = "userId";
    public static final String KEY_CURRENT_USER = "currentUser";
    public static final String KEY_LAST_SYNC_DATE = "lastSyncDate";
    public static final String KEY_IS_AUTO_SYNC_REQUIRED = "isAutoSyncRequired";
    public static final String KEY_IS_DATA_BACKUP_REQUIRED = "isDataBackupRequired";
    public static final String KEY_UNIQUE_DEVICE_ID = "uniqueDeviceId";
    public static final String KEY_LOG_LEVEL = "logLevel";
    public static final String KEY_IS_LOG_FILE_REQUIRED = "isLogFileRequired";
    public static final String KEY_SERVER_TIME_ZONE = "serverTimeZone";
    public static final String KEY_DEVICE_IMEI = "deviceIMEI";


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

    public LoginResponseDTO getCurrentUser() {
        Gson gson = new Gson();
        String json = getString(KEY_CURRENT_USER, "");
        LoginResponseDTO user = gson.fromJson(json, LoginResponseDTO.class);
        return user;
    }

    public void deleteEmployeeUserName() {
        deletePref(KEY_USER_NAME);
    }

    public void deleteEmployeePassword() {
        deletePref(KEY_USER_PASSWORD);
    }

    public void deleteEmployeeAuthToken() {
        deletePref(KEY_AUTHENTICATION_TOKEN);
    }

    public void deleteEmployeeId() {
        deletePref(KEY_USER_Id);
    }

    public void deleteCurrentUser() {
        deletePref(KEY_CURRENT_USER);
    }

}
