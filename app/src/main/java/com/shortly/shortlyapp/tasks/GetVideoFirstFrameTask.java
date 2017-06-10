package com.shortly.shortlyapp.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.shortly.shortlyapp.utils.VideoUtil;


/**
 * Created by Bashir on 1/12/16.
 */
public class GetVideoFirstFrameTask extends AsyncTask<Void, Void, Bitmap> {
    private GetFirstFrameListener mCallback;
    private String mVideoURL;

    public GetVideoFirstFrameTask(String videoURL, GetFirstFrameListener callback) {
        this.mVideoURL = videoURL;
        this.mCallback = callback;
    }

    @Override
    protected Bitmap doInBackground(Void... urls) {
        //return VideoUtil.getVideoFirstFrame(mVideoURL);
        return VideoUtil.getFirstFrameUsingThumbnailUtil(mVideoURL);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mCallback.onGetFirstFrameComplete(bitmap);
    }

    public interface GetFirstFrameListener {
        void onGetFirstFrameComplete(Bitmap bitmap);
    }
}