package com.shortly.shortlyapp.UI.Activities.VideoDetail;

import android.os.Bundle;
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
    private boolean mIsWatchLater;
    int mVideoPlayTime;


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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mVideoPlayerView = (VideoPlayerView) findViewById(R.id.videoView);

        //get video id from intent
        mVideoId = 1;
        fetchVideoDetails();
        mImageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
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
        mVideoPlayTime = mVideoPlayerView.getCurrentTime();
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

//        if(currentTime>0){
//            videoPlayerView.setCurrentTime(currentTime);
//
//        }
//        videoPlayerView.setCurrentTime(50);
//        videoPlayerView.play();


    }

//    @Override
//    public void onBackPressed() {
//        if (mVideoPlayerView != null && mVideoPlayerView.isPlaying()) {
//            mVideoPlayTime = mVideoPlayerView.getCurrentTime();
//            mVideoPlayerView.stop();
//        } else {
//            super.onBackPressed();
//        }
//
//    }

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


            if (mIsVideoLiked) {
                mLikeButton.setSelected(true);
            }

            if (mIsWatchLater) {
                mWatchLaterButton.setSelected(true);
            }

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

    private void playVideo() {
        String videoPath = mVideoDetail.getPath();
        if (videoPath != null && !videoPath.isEmpty()) {
            mVideoDetailLayout.setVisibility(View.GONE);
            mEmptyViewLayout.setVisibility(View.GONE);
            mVideoPlayerView.setVisibility(View.VISIBLE);

            mVideoPlayerView.setVideoPath(videoPath, false);
            mVideoPlayerView.setCurrentTime(mVideoPlayTime);
            ProgressHandler.showProgressDialog(this, getString(R.string.app_name), "Processing...", 0, Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED, "", "");
            mVideoPlayerView.playVideo();
        }

    }

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
                        }
                    });
                    APICalls.likeVideo(mVideoId, VideoDetailActivity.this);
                }


            }.start();

            mLikeButton.setSelected(true);
        }
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
                    }
                });
                APICalls.addVideoToWatchLater(mVideoId, mVideoPlayTime, VideoDetailActivity.this);
            }


        }.start();
        mWatchLaterButton.setSelected(true);
    }
}
