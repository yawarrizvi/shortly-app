package com.shortly.shortlyapp.UI.Activities.VideoDetail;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shortly.shortlyapp.Interfaces.SyncInterface;
import com.shortly.shortlyapp.Logic.ProgressHandler.ProgressHandler;
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.Sync.APICalls;
import com.shortly.shortlyapp.UI.Activities.BaseActivity;
import com.shortly.shortlyapp.model.VideoDetailResponse;
import com.shortly.shortlyapp.utils.Constants;
import com.shortly.shortlyapp.utils.Utilities;
import com.shortly.shortlyapp.widgets.VideoPlayerView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;

public class VideoDetailActivity extends BaseActivity {

    private static VideoDetailResponse mVideoDetail;
    private int mVideoId;
    private boolean mIsVideoLiked;
    private int mIsWatchLater;
    int mVideoPlayTime;
    private static Handler mHandler;
    public Activity mActivity;

    @Bind(R.id.empty_view_layout)
    LinearLayout mEmptyViewLayout;

    @Bind(R.id.video_detail_layout)
    LinearLayout mVideoDetailLayout;

    @Bind(R.id.img_view_thumbnail)
    ImageView mImageViewThumbnail;

    @Bind(R.id.btn_like)
    Button mLikeButton;

    @Bind(R.id.btn_watch_later)
    Button mWatchLaterButton;

    @Bind(R.id.title)
    TextView mTxtViewTitle;

    @Bind(R.id.video_detail)
    TextView mTxtViewVideoDetail;

    @Bind(R.id.video_genre)
    TextView mTxtViewGenre;

    @Bind(R.id.credits)
    TextView mTxtViewCredits;


    VideoPlayerView mVideoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_detail);
        mActivity = VideoDetailActivity.this;
        Intent intent = this.getIntent();
        if (intent.hasExtra("videoId")) {
            mVideoId = intent.getIntExtra("videoId", 0);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mVideoPlayerView = (VideoPlayerView) findViewById(R.id.videoView);
        mVideoPlayerView.setVisibility(View.GONE);

        mVideoDetailLayout.setVisibility(View.GONE);
        fetchVideoDetails();
        mImageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoPath = mVideoDetail.getPath();
                playVideo(videoPath);
//                prepareVideo();

            }
        });
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeVideo();
            }
        });
        mWatchLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVideoToWatchLater();
            }
        });
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //    @Override
    public void onBackPressed() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (mVideoPlayerView != null && mVideoPlayerView.isPlaying()) { //TODO:handle video end scenario
            setButtonConfig();
            mVideoPlayTime = mVideoPlayerView.getCurrentTime();
            mVideoPlayerView.stop();
            mVideoDetailLayout.setVisibility(View.VISIBLE);
            mEmptyViewLayout.setVisibility(View.GONE);
            mVideoPlayerView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void fetchVideoDetails() {
        ProgressHandler.showProgressDialog(this, getString(R.string.app_name), "Loading...", 0, Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED, "", "");
        new Thread() {
            public void run() {
                APICalls.setSyncInterface(new SyncInterface() {
                    @Override
                    public void onAPIResult(int result, Object resultObject, int totalRecords) {
                        switch (result) {
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
                                loadVideoDetails(resultObject);
                                break;
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                showError(result);
                                showEmptyView();
                                break;
                            default:
                                ProgressHandler.hideProgressDialogue();
                                break;
                        }
                    }
                });
                APICalls.fetchVideoDetail(mVideoId, VideoDetailActivity.this);
            }


        }.start();
    }

    private void showEmptyView() {
        mVideoDetailLayout.setVisibility(View.GONE);
        mEmptyViewLayout.setVisibility(View.VISIBLE);
//        ProgressHandler.hideProgressDialogue();

    }

    private void loadVideoDetails(Object resultObject) {
        if (resultObject != null) {
            mVideoDetail = (VideoDetailResponse) resultObject;
            mIsVideoLiked = mVideoDetail.getLiked();
            mIsWatchLater = mVideoDetail.getLater();
            mVideoPlayTime = mVideoDetail.getTime();
            setButtonConfig();

            String title = mVideoDetail.getTitle();
            String casts = mVideoDetail.getCasts();
            String category = mVideoDetail.getCategory();
            String description = mVideoDetail.getDescription();
            String duration = mVideoDetail.getDuration();
            duration = category + ", " + Utilities.getMovieDurationString(this, Integer.parseInt(duration));

            mTxtViewCredits.setText(casts);
            mTxtViewTitle.setText(title);
            mTxtViewGenre.setText(duration);
            mTxtViewVideoDetail.setText(description);

            Picasso.with(this).load(mVideoDetail.getThumbnails()).into(mImageViewThumbnail);
            mVideoDetailLayout.setVisibility(View.VISIBLE);
            mEmptyViewLayout.setVisibility(View.GONE);
            ProgressHandler.hideProgressDialogue();
        }
    }


    private void playVideo(final String videoPath) {

        if (videoPath != null && !videoPath.isEmpty()) {
            mVideoDetailLayout.setVisibility(View.GONE);
            mEmptyViewLayout.setVisibility(View.GONE);
            mVideoPlayerView.setVisibility(View.VISIBLE);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    mVideoPlayerView.setVideoPath(videoPath, false, VideoDetailActivity.this);
                    mVideoPlayerView.setCurrentTime(mVideoPlayTime);
                    mVideoPlayerView.playVideo(VideoDetailActivity.this);
                }
            }, 500);

        }
    }

//    private void prepareVideo() {
//        if (mHandler != null) {
//            mHandler = null;
//        }
//        mHandler = new Handler(this.getMainLooper());
//        Runnable changeMessage = new Runnable() {
//            @Override
//            public void run() {
//                mVideoDetailLayout.setVisibility(View.GONE);
//                mEmptyViewLayout.setVisibility(View.GONE);
//                mVideoPlayerView.setVisibility(View.VISIBLE);
//                playVideo();
//            }
//        };
//        mHandler.post(changeMessage);
//    }
//
//    private void playVideo() {
//        final String videoPath = mVideoDetail.getPath();
//        /*if (videoPath != null && !videoPath.isEmpty()) {
//            mVideoPlayerView.setVideoPath(videoPath, false, VideoDetailActivity.this);
//            mVideoPlayerView.setCurrentTime(mVideoPlayTime);
//            startVideo();
//        }*/
//        new Thread() {
//            public void run() {
//                if (videoPath != null && !videoPath.isEmpty()) {
//                    mVideoPlayerView.setVideoPath(videoPath, false, mActivity);
//                    mVideoPlayerView.setCurrentTime(mVideoPlayTime);
//                    startVideo();
//                }
//            }
//        }.start();
//    }
//
//    private void startVideo() {
//        if (mHandler != null) {
//            mHandler = null;
//        }
//        mHandler = new Handler(this.getMainLooper());
//        Runnable changeMessage = new Runnable() {
//            @Override
//            public void run() {
//                mVideoPlayerView.playVideo(VideoDetailActivity.this);
//            }
//        };
//        mHandler.post(changeMessage);
//    }

    private void likeVideo() {
        if (!mVideoDetail.getLiked()) {
            ProgressHandler.showProgressDialog(this, getString(R.string.app_name), "Processing...", 0, Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED, "", "");
            new Thread() {
                public void run() {
                    APICalls.setSyncInterface(new SyncInterface() {
                        @Override
                        public void onAPIResult(int result, Object resultObject, int totalRecords) {
                            switch (result) {
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
                                    showResponse("Video Liked!");
                                    mVideoDetail.setLiked(true);
                                    mLikeButton.setPressed(true);
                                    break;
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                    showResponse("An error occurred. Please try again");
                                    break;
                                default:
                                    ProgressHandler.hideProgressDialogue();
                                    break;
                            }
                            setButtonConfig();
                        }
                    });
                    APICalls.likeVideo(mVideoId, VideoDetailActivity.this);
                }


            }.start();
        }
        setButtonConfig();
    }

    private void showResponse(final String message) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ProgressHandler.upDateProgressDialog(VideoDetailActivity.this, "", message, 0, Constants.ProgressBarStyles.PROGRESS_BAR_NONE, getString(R.string.button_title_ok), "");
                }
            }
        };
        timerThread.start();
    }

    private void addVideoToWatchLater() {
        ProgressHandler.showProgressDialog(this, getString(R.string.app_name), "Loading...", 0, Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED, "", "");
        new Thread() {
            public void run() {
                APICalls.setSyncInterface(new SyncInterface() {
                    @Override
                    public void onAPIResult(int result, Object resultObject, int totalRecords) {
                        switch (result) {
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
                                showResponse("Video added to watch later list!");
                                mVideoDetail.setLater(1);
                                mWatchLaterButton.setPressed(true);
                                break;
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                showError(result);
                                break;
                            default:
                                ProgressHandler.hideProgressDialogue();
                                break;
                        }
                        setButtonConfig();
                    }
                });
                APICalls.addVideoToWatchLater(mVideoId, mVideoPlayTime, VideoDetailActivity.this);
            }
        }.start();
    }

    public void updateVideoEnd() {
        ProgressHandler.showProgressDialog(this, getString(R.string.app_name), "Loading...", 0, Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED, "", "");
        new Thread() {
            public void run() {
                APICalls.setSyncInterface(new SyncInterface() {
                    @Override
                    public void onAPIResult(int result, Object resultObject, int totalRecords) {
                        switch (result) {
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
//                                showResponse("Video added to watch later list!");
                                mWatchLaterButton.setPressed(true);
                                break;
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                showError(result);
                                break;
                            default:
                                ProgressHandler.hideProgressDialogue();
                                break;
                        }
                        setButtonConfig();
                    }
                });
                APICalls.pushVideoEnd(mVideoId, VideoDetailActivity.this);
            }
        }.start();
    }

    private void showError(int errorType) {
        final String message;
        switch (errorType) {
            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                message = getString(R.string.key_error_no_connectivity);
                break;
            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                message = getString(R.string.key_error_service_failure);
                break;
            default:
                message = getString(R.string.key_error_service_failure);
                break;
        }

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ProgressHandler.upDateProgressDialog(VideoDetailActivity.this, "", message, 0, Constants.ProgressBarStyles.PROGRESS_BAR_NONE, getString(R.string.button_title_ok), "");
                }
            }
        };
        timerThread.start();
    }

    private void setButtonConfig() {
        if (mVideoDetail != null) {
            if (mLikeButton != null) {
                mLikeButton.setPressed(mVideoDetail.getLiked());
            }
            if (mWatchLaterButton != null) {
                boolean isPressed = false;
                if ((mVideoDetail.getLater() == 1)) {
                    isPressed = true;
                }
                mWatchLaterButton.setPressed(isPressed);
            }
        }
    }
}