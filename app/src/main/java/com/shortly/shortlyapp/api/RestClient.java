package com.shortly.shortlyapp.api;

import com.shortly.shortlyapp.BuildConfig;
import com.shortly.shortlyapp.model.DefaultDTO;
import com.shortly.shortlyapp.model.GenreListDTO;
import com.shortly.shortlyapp.model.LoginResponseDTO;
import com.shortly.shortlyapp.model.MostViewedListDTO;
import com.shortly.shortlyapp.model.VideoDetailDTO;
import com.shortly.shortlyapp.utils.WebUrls;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by yarizvi on 08/06/2017.
 */

public class RestClient {
    private static final String LOG_TAG = RestClient.class.getSimpleName();
    private static ShortlyApiInterface iShortlyService;
    private static OkHttpClientExt.Builder mHttpClient;
    private static Dispatcher mDispatcher;
    private static Retrofit mRestAdapter;
    private static final int TIMEOUT = 300; //timeout in seconds

    /**
     * Get the client of FieldForce database
     *
     * @return api defined by FFAPIInterface
     */
    public static ShortlyApiInterface getShortlyClient(String serviceURL) {
//        if (iShortlyService != null) {
//            iShortlyService = null;
//        }
//        if (iShortlyService == null) {
        String servicePath = WebUrls.SERVICE_BASE_URL + serviceURL;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        }

        if (mDispatcher == null) {
            mDispatcher = new Dispatcher();
        }

        if (mHttpClient == null) {
            mHttpClient = new OkHttpClientExt.Builder();
            mHttpClient.addInterceptor(logging);
            mHttpClient.dispatcher(mDispatcher);
        }

        if (mRestAdapter == null || !mRestAdapter.baseUrl().toString().equals(servicePath)) {
            mRestAdapter = new Retrofit.Builder()
                    .baseUrl(servicePath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mHttpClient.readTimeout(TIMEOUT, TimeUnit.SECONDS).connectTimeout(TIMEOUT, TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS).build())
                    .build();
            iShortlyService = mRestAdapter.create(ShortlyApiInterface.class);

        }
        return iShortlyService;
    }

    public static OkHttpClientExt.Builder getHttpClient() {
        return mHttpClient;
    }

    public static Dispatcher getDispatcher() {
        return mDispatcher;
    }

    public interface ShortlyApiInterface {
        @POST("users/login")
        @Headers({
                "Accept:application/json",
                "Content-Type:text/plain; charset=utf-8"
        })
        Call<LoginResponseDTO> authenticateUser(@Body HashMap<String, Object> loginInfo);

        @POST("users/signup")
        @Headers({
                "Accept:application/json",
                "Content-Type:text/plain; charset=utf-8"
        })
        Call<LoginResponseDTO> registerUser(@Body HashMap<String, Object> loginInfo);


        @POST("watch/video/{videoID}")
        @Headers({
                "Accept:application/json",
                "Content-Type:text/plain; charset=utf-8"
        })
        Call<VideoDetailDTO> fetchVideoDetail(@Header("X-Authentication-Token") String authToken, @Path("videoID") int videoID);

        @POST("watch/video/{timePlayed}/{VideoId}")
        @Headers({
                "Accept:application/json",
                "Content-Type:text/plain; charset=utf-8"
        })
        Call<DefaultDTO> pushVideoPlayedTime(@Header("X-Authentication-Token") String authToken, @Path("timePlayed") int timePlayed, @Path("VideoId") int videoID);

        @POST("watch/video/{userId}/{VideoId}")
        @Headers({
                "Accept:application/json",
                "Content-Type:text/plain; charset=utf-8"
        })
        Call<DefaultDTO> pushVideoEnd(@Header("X-Authentication-Token") String authToken, @Path("userId") int userId, @Path("VideoId") int videoID);

        @POST("later/video/end")
        @Headers({
                "Accept:application/json",
                "Content-Type:text/plain; charset=utf-8"
        })
        Call<DefaultDTO> addVideoToWatchLater(@Header("X-Authentication-Token") String authToken, @Body HashMap<String, Object> requestParameters);

        @POST("api/like/video")
        @Headers({
                "Accept:application/json",
                "Content-Type:text/plain; charset=utf-8"
        })
        Call<DefaultDTO> likeVideo(@Header("X-Authentication-Token") String authToken, @Body HashMap<String, Object> requestParameters);

        @POST("api/categories/list")
        @Headers({
                "Accept:application/json",
                "Content-Type:text/plain; charset=utf-8"
        })
        Call<GenreListDTO> getGenreList(@Header("X-Authentication-Token") String authToken);

        @POST("api/recent/videos")
        @Headers({
                "Accept:application/json",
                "Content-Type:text/plain; charset=utf-8"
        })
        Call<MostViewedListDTO> getMostViewedVideos(@Header("X-Authentication-Token") String authToken);

        @POST("/api/featured/videos")
        @Headers({
                "Accept:application/json",
                "Content-Type:text/plain; charset=utf-8"
        })
        Call<MostViewedListDTO> getFeaturedVideos(@Header("X-Authentication-Token") String authToken);


    }
}
