package com.shortly.shortlyapp.utils;

/**
 * Created by yarizvi on 06/06/2017.
 */

public class Constants {
    //Request Parameters Keys
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CONFIRM_PASSWORD = "password_confirmation";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_VIDEO_ID = "video_id";
    public static final String KEY_TIME = "time";
    public static final String KEY_TIME_PLAYED = "time_played";
    public static final String KEY_PAGE = "page";
    public static final String KEY_Id = "id";


    public interface ProgressBarStyles {
        int PROGRESS_BAR_NONE = 0;
        int PROGRESS_BAR_ANIMATED = 1;
        int PROGRESS_BAR_SIMPLE = 2;
    }

    public interface ServiceResponseCodes {
        int RESPONSE_CODE_UNAUTHORIZED_USER = 401;
        int RESPONSE_CODE_SUCCESS = 200;
        int RESPONSE_CODE_SERVICE_FAILURE = 45;
        int RESPONSE_CODE_NO_CONNECTIVITY = 25;
        int RESPONSE_CODE_ERROR = 3;
    }

}
