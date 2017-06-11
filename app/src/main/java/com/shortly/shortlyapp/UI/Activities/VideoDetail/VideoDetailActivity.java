package com.shortly.shortlyapp.UI.Activities.VideoDetail;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shortly.shortlyapp.Interfaces.SyncInterface;
import com.shortly.shortlyapp.Logic.ProgressHandler.ProgressHandler;
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.Sync.APICalls;
import com.shortly.shortlyapp.UI.Activities.BaseActivity;
import com.shortly.shortlyapp.model.VideoDetailResponse;
import com.shortly.shortlyapp.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.Bind;

public class VideoDetailActivity extends BaseActivity {

    private static VideoDetailResponse mVideoDetail;
    private int mVideoId;

    @Bind(R.id.empty_view_layout)
    LinearLayout mEmptyViewLayout;

    @Bind(R.id.video_detail_layout)
    LinearLayout mVideoDetailLayout;

    @Bind(R.id.img_view_thumbnail)
    ImageView mImageViewThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get video id from intent
        mVideoId = 32;
        fetchVideoDetails();
    }

    private void fetchVideoDetails() {
        ProgressHandler.showProgressDialog(this, getString(R.string.app_name), getString(R.string.key_authentication_title), 0, Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED, "", "");
        new Thread() {
            public void run() {
                APICalls.setSyncInterface(new SyncInterface() {
                    @Override
                    public void onAPIResult(int result, Object resultObject) {
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

        ProgressHandler.hideProgressDialogue();
    }

    private void loadVideoDetails(Object resultObject) {
        mVideoDetail = (VideoDetailResponse) resultObject;

        String description = mVideoDetail.getDescription();
        String duration = mVideoDetail.getDuration();

        Picasso.with(this).load(mVideoDetail.getThumbnails()).into(mImageViewThumbnail);
        mVideoDetailLayout.setVisibility(View.VISIBLE);
        mEmptyViewLayout.setVisibility(View.GONE);
        ProgressHandler.hideProgressDialogue();
    }
}
