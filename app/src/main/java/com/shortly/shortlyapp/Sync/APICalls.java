package com.shortly.shortlyapp.Sync;

import android.content.Context;
import android.util.Log;

import com.shortly.shortlyapp.DataProvider.Prefs;
import com.shortly.shortlyapp.Interfaces.SyncInterface;
import com.shortly.shortlyapp.Network.NetworkManager;
import com.shortly.shortlyapp.api.RestClient;
import com.shortly.shortlyapp.model.DefaultDTO;
import com.shortly.shortlyapp.model.GenreListDTO;
import com.shortly.shortlyapp.model.GenreListResponse;
import com.shortly.shortlyapp.model.LoginResponse;
import com.shortly.shortlyapp.model.LoginResponseDTO;
import com.shortly.shortlyapp.model.MostViewedListDTO;
import com.shortly.shortlyapp.model.MostViewedListResponse;
import com.shortly.shortlyapp.model.VideoDetailDTO;
import com.shortly.shortlyapp.model.VideoDetailResponse;
import com.shortly.shortlyapp.utils.Constants;
import com.shortly.shortlyapp.utils.WebUrls;

import java.util.HashMap;
import java.util.List;

import okhttp3.Dispatcher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yarizvi on 08/06/2017.
 */

public class APICalls {
    private static SyncInterface mSyncInterface;


    public static void authenticateUser(final String username, final String password, final Context context) {
        if (!NetworkManager.isConnected(context)) {
            stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY);
        } else {
            HashMap<String, Object> requestParameters = new HashMap<>();
            requestParameters.put(Constants.KEY_EMAIL, username);
            requestParameters.put(Constants.KEY_PASSWORD, password);

            RestClient.ShortlyApiInterface service = RestClient.getShortlyClient(WebUrls.SERVICE_NAME);
            Call<LoginResponseDTO> employeeCall = service.authenticateUser(requestParameters);
            employeeCall.enqueue(new Callback<LoginResponseDTO>() {
                                     @Override
                                     public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                                         if (response.isSuccessful()) {
                                             LoginResponseDTO loginResponseDTO = response.body();
                                             int status = 0;
                                             LoginResponse user = null;
                                             if (loginResponseDTO != null) {
                                                 status = loginResponseDTO.getMeta().getStatus();
                                                 if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS) {
                                                     List<LoginResponse> loginResponses = loginResponseDTO.getResponse();
                                                     if (!loginResponses.isEmpty()) {
                                                         user = loginResponses.get(0);
                                                     }
                                                     DataPersistence.setUserDetailsInSharedPreferences(user, password, context);
                                                     mSyncInterface.onAPIResult(Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS, null);
                                                 } else if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER) {
                                                     stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER);
                                                 } else {
                                                     stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                                                 }
                                             } else {
                                                 stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                             }
                                         } else {
                                             stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                         }
                                     }

                                     @Override
                                     public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                                         // there is more than just a failing request (like: no internet connection)
                                         stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);

                                     }
                                 }

            );

        }
    }

    public static void registerUser(final String username, final String password, final String confirmPassword, final Context context) {
        if (!NetworkManager.isConnected(context)) {
            stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY);
        } else {
            HashMap<String, Object> requestParameters = new HashMap<>();
            requestParameters.put(Constants.KEY_EMAIL, username);
            requestParameters.put(Constants.KEY_PASSWORD, password);
            requestParameters.put(Constants.KEY_CONFIRM_PASSWORD, confirmPassword);

            RestClient.ShortlyApiInterface service = RestClient.getShortlyClient(WebUrls.SERVICE_NAME);
            Call<LoginResponseDTO> employeeCall = service.registerUser(requestParameters);
            employeeCall.enqueue(new Callback<LoginResponseDTO>() {
                                     @Override
                                     public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                                         if (response.isSuccessful()) {
                                             LoginResponseDTO loginResponseDTO = response.body();
                                             int status = 0;
                                             LoginResponse user = null;
                                             if (loginResponseDTO != null) {
                                                 status = loginResponseDTO.getMeta().getStatus();
                                                 if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS) {
                                                     List<LoginResponse> loginResponses = loginResponseDTO.getResponse();
                                                     if (!loginResponses.isEmpty()) {
                                                         user = loginResponses.get(0);
                                                     }
                                                     DataPersistence.setUserDetailsInSharedPreferences(user, password, context);
                                                     mSyncInterface.onAPIResult(Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS, null);
                                                 } else if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER) {
                                                     stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER);
                                                 } else {
                                                     stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                                                 }
                                             } else {
                                                 stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                             }
                                         } else {
                                             stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                         }
                                     }

                                     @Override
                                     public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                                         // there is more than just a failing request (like: no internet connection)
                                         stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);

                                     }
                                 }

            );

        }
    }

    //Top Cell Data
    public static void getFeaturedVideos(final Context context) {
        if (!NetworkManager.isConnected(context)) {
            stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY);
        } else {
            Prefs prefs = Prefs.getInstance(context);
            String authToken = prefs.getAuthenticationToken();
            RestClient.ShortlyApiInterface service = RestClient.getShortlyClient(WebUrls.SERVICE_NAME);
            Call<MostViewedListDTO> getMostViewedVideosCall = service.getMostViewedVideos(authToken);
            getMostViewedVideosCall.enqueue(new Callback<MostViewedListDTO>() {
                @Override
                public void onResponse(Call<MostViewedListDTO> call, Response<MostViewedListDTO> response) {
                    if (response.isSuccessful()) {
                        MostViewedListDTO mostViewedListDTO = response.body();
                        int status = 0;
                        if (mostViewedListDTO != null) {
                            status = mostViewedListDTO.getMeta().getStatus();
                            if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS) {
                                List<MostViewedListResponse> genreResponseList = mostViewedListDTO.getResponse();
                                mSyncInterface.onAPIResult(Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS, genreResponseList);
                            } else {
                                stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                            }
                        }
                    } else {
                        stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                    }
                }
                @Override
                public void onFailure(Call<MostViewedListDTO> call, Throwable t) {
                    // there is more than just a failing request (like: no internet connection)
                    stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                }
            });
        }
    }

    public static void fetchVideoDetail(final int videoId, final Context context) {
        if (!NetworkManager.isConnected(context)) {
            stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY);
        } else {

            Prefs prefs = Prefs.getInstance(context);
            String authToken = prefs.getAuthenticationToken();
            HashMap<String, Object> requestParameters = new HashMap<>();

            requestParameters.put(Constants.KEY_Id, videoId);
            RestClient.ShortlyApiInterface service = RestClient.getShortlyClient(WebUrls.SERVICE_NAME);
            Call<VideoDetailDTO> videoDetailCall = service.fetchVideoDetail((authToken), videoId);
            videoDetailCall.enqueue(new Callback<VideoDetailDTO>() {
                @Override
                public void onResponse(Call<VideoDetailDTO> call, Response<VideoDetailDTO> response) {
                    if (response.isSuccessful()) {
                        VideoDetailDTO videoDetailDTO = response.body();
                        int status = 0;
                        if (videoDetailDTO != null) {
                            status = videoDetailDTO.getMeta().getStatus();
                            if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS) {
                                List<VideoDetailResponse> detailResponseList = videoDetailDTO.getResponse();
                                VideoDetailResponse detailResponse = null;
                                if (detailResponseList != null && detailResponseList.size() > 0) {
                                    detailResponse = detailResponseList.get(0);
                                }
                                mSyncInterface.onAPIResult(Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS, detailResponse);
                            } else {
                                stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                            }
                        }
                    } else {
                        stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                    }
                }

                @Override
                public void onFailure(Call<VideoDetailDTO> call, Throwable t) {
                    // there is more than just a failing request (like: no internet connection)
                    stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                }
            });
        }
    }


    public static void pushVideoPlayedTime(final int videoId, int timePlayed, final Context context) {
        if (!NetworkManager.isConnected(context)) {
            stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY);
        } else {

            Prefs prefs = Prefs.getInstance(context);
            String authToken = prefs.getAuthenticationToken();
            RestClient.ShortlyApiInterface service = RestClient.getShortlyClient(WebUrls.SERVICE_NAME);
            Call<DefaultDTO> pushPlayedTimeCall = service.pushVideoPlayedTime((authToken), timePlayed, videoId);
            pushPlayedTimeCall.enqueue(new Callback<DefaultDTO>() {
                                           @Override
                                           public void onResponse(Call<DefaultDTO> call, Response<DefaultDTO> response) {
                                               if (response.isSuccessful()) {
                                                   DefaultDTO videoDetailDTO = response.body();
                                                   int status = 0;
                                                   if (videoDetailDTO != null) {
                                                       status = videoDetailDTO.getMeta().getStatus();
                                                       if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS) {
                                                           mSyncInterface.onAPIResult(Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS, null);
                                                       } else {
                                                           stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                                                       }
                                                   }
                                               } else {
                                                   stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                               }
                                           }

                                           @Override
                                           public void onFailure(Call<DefaultDTO> call, Throwable t) {
                                               // there is more than just a failing request (like: no internet connection)
                                               stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                           }
                                       }
            );
        }
    }

    public static void pushVideoEnd(final int videoId, final Context context) {
        if (!NetworkManager.isConnected(context)) {
            stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY);
        } else {

            Prefs prefs = Prefs.getInstance(context);
            String authToken = prefs.getAuthenticationToken();
            int userId = prefs.getUserId();
            RestClient.ShortlyApiInterface service = RestClient.getShortlyClient(WebUrls.SERVICE_NAME);
            Call<DefaultDTO> pushVideoEndCall = service.pushVideoEnd((authToken), userId, videoId);
            pushVideoEndCall.enqueue(new Callback<DefaultDTO>() {
                                         @Override
                                         public void onResponse(Call<DefaultDTO> call, Response<DefaultDTO> response) {
                                             if (response.isSuccessful()) {
                                                 DefaultDTO videoDetailDTO = response.body();
                                                 int status = 0;
                                                 if (videoDetailDTO != null) {
                                                     status = videoDetailDTO.getMeta().getStatus();
                                                     if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS) {
                                                         mSyncInterface.onAPIResult(Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS, null);
                                                     } else {
                                                         stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                                                     }
                                                 }
                                             } else {
                                                 stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                             }
                                         }

                                         @Override
                                         public void onFailure(Call<DefaultDTO> call, Throwable t) {
                                             // there is more than just a failing request (like: no internet connection)
                                             stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                         }
                                     }
            );
        }
    }

    public static void addVideoToWatchLater(final int videoId, int timePlayed, final Context context) {
        if (!NetworkManager.isConnected(context)) {
            stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY);
        } else {

            Prefs prefs = Prefs.getInstance(context);
            String authToken = prefs.getAuthenticationToken();
            int userId = prefs.getUserId();

            HashMap<String, Object> requestParameters = new HashMap<>();
            requestParameters.put(Constants.KEY_USER_ID, userId);
            requestParameters.put(Constants.KEY_VIDEO_ID, videoId);
            requestParameters.put(Constants.KEY_TIME_PLAYED, timePlayed);


            RestClient.ShortlyApiInterface service = RestClient.getShortlyClient(WebUrls.SERVICE_NAME);
            Call<DefaultDTO> addVideoToWatchLaterCall = service.addVideoToWatchLater((authToken), requestParameters);
            addVideoToWatchLaterCall.enqueue(new Callback<DefaultDTO>() {
                                                 @Override
                                                 public void onResponse(Call<DefaultDTO> call, Response<DefaultDTO> response) {
                                                     if (response.isSuccessful()) {
                                                         DefaultDTO videoDetailDTO = response.body();
                                                         int status = 0;
                                                         if (videoDetailDTO != null) {
                                                             status = videoDetailDTO.getMeta().getStatus();
                                                             if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS) {
                                                                 mSyncInterface.onAPIResult(Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS, null);
                                                             } else {
                                                                 stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                                                             }
                                                         }
                                                     } else {
                                                         stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                                     }
                                                 }

                                                 @Override
                                                 public void onFailure(Call<DefaultDTO> call, Throwable t) {
                                                     // there is more than just a failing request (like: no internet connection)
                                                     stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                                 }
                                             }
            );
        }
    }

    public static void likeVideo(final int videoId, final Context context) {
        if (!NetworkManager.isConnected(context)) {
            stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY);
        } else {

            Prefs prefs = Prefs.getInstance(context);
            String authToken = prefs.getAuthenticationToken();
            int userId = prefs.getUserId();

            HashMap<String, Object> requestParameters = new HashMap<>();
            requestParameters.put(Constants.KEY_USER_ID, userId);
            requestParameters.put(Constants.KEY_VIDEO_ID, videoId);

            RestClient.ShortlyApiInterface service = RestClient.getShortlyClient(WebUrls.SERVICE_NAME);
            Call<DefaultDTO> addVideoToWatchLaterCall = service.addVideoToWatchLater((authToken), requestParameters);
            addVideoToWatchLaterCall.enqueue(new Callback<DefaultDTO>() {
                                                 @Override
                                                 public void onResponse(Call<DefaultDTO> call, Response<DefaultDTO> response) {
                                                     if (response.isSuccessful()) {
                                                         DefaultDTO videoDetailDTO = response.body();
                                                         int status = 0;
                                                         if (videoDetailDTO != null) {
                                                             status = videoDetailDTO.getMeta().getStatus();
                                                             if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS) {
                                                                 mSyncInterface.onAPIResult(Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS, null);
                                                             } else {
                                                                 stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                                                             }
                                                         }
                                                     } else {
                                                         stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                                     }
                                                 }

                                                 @Override
                                                 public void onFailure(Call<DefaultDTO> call, Throwable t) {
                                                     // there is more than just a failing request (like: no internet connection)
                                                     stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                                                 }
                                             }
            );
        }
    }

    public static void getCategoryList(final Context context) {
        if (!NetworkManager.isConnected(context)) {
            stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY);
        } else {
            Prefs prefs = Prefs.getInstance(context);
            String authToken = prefs.getAuthenticationToken();
            RestClient.ShortlyApiInterface service = RestClient.getShortlyClient(WebUrls.SERVICE_NAME);
            Call<GenreListDTO> getCategoriesCall = service.getGenreList(authToken);
            getCategoriesCall.enqueue(new Callback<GenreListDTO>() {
              @Override
              public void onResponse(Call<GenreListDTO> call, Response<GenreListDTO> response) {
                  if (response.isSuccessful()) {
                      GenreListDTO genreListDTO = response.body();
                      int status = 0;
                      if (genreListDTO != null) {
                          status = genreListDTO.getMeta().getStatus();
                          if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS) {
                              List<GenreListResponse> genreResponseList = genreListDTO.getResponse();
                              mSyncInterface.onAPIResult(Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS, genreResponseList);
                          } else {
                              stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                          }
                      }
                  } else {
                      stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                  }
              }

              @Override
              public void onFailure(Call<GenreListDTO> call, Throwable t) {
                  // there is more than just a failing request (like: no internet connection)
                  stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
              }
          });
        }
    }

    public static void getMostViewedVideos(final Context context) {
        if (!NetworkManager.isConnected(context)) {
            stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY);
        } else {
            Prefs prefs = Prefs.getInstance(context);
            String authToken = prefs.getAuthenticationToken();
            RestClient.ShortlyApiInterface service = RestClient.getShortlyClient(WebUrls.SERVICE_NAME);
            Call<MostViewedListDTO> getMostViewedVideosCall = service.getMostViewedVideos(authToken);
            getMostViewedVideosCall.enqueue(new Callback<MostViewedListDTO>() {
                @Override
                public void onResponse(Call<MostViewedListDTO> call, Response<MostViewedListDTO> response) {
                    if (response.isSuccessful()) {
                        MostViewedListDTO mostViewedListDTO = response.body();
                        int status = 0;
                        if (mostViewedListDTO != null) {
                            status = mostViewedListDTO.getMeta().getStatus();
                            if (status == Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS) {
                                List<MostViewedListResponse> genreResponseList = mostViewedListDTO.getResponse();
                                mSyncInterface.onAPIResult(Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS, genreResponseList);
                            } else {
                                stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                            }
                        }
                    } else {
                        stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                    }
                }
                @Override
                public void onFailure(Call<MostViewedListDTO> call, Throwable t) {
                    // there is more than just a failing request (like: no internet connection)
                    stopSyncDownloadProcess(context, Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE);
                }
            });
        }
    }

    private static void stopSyncDownloadProcess(Context context, int responseCode) {
        cancelAllRequests();
        mSyncInterface.onAPIResult(responseCode, null);

    }

    private static void cancelAllRequests() {
        int activeCalls = getCurrentActiveRequestsCount();
        Log.v("Sync Dload Cancel All: ", activeCalls + "");
        Dispatcher dispatcher = RestClient.getDispatcher();
        if (dispatcher != null) {
            dispatcher.cancelAll();
        }
    }

    private static int getCurrentActiveRequestsCount() {
        int activeCalls = 0;
        Dispatcher dispatcher = RestClient.getDispatcher();
        if (dispatcher != null) {
            activeCalls = dispatcher.runningCallsCount();
        }
        return activeCalls;
    }
    public static void setSyncInterface(SyncInterface syncInterface) {
        mSyncInterface = syncInterface;
    }
}
