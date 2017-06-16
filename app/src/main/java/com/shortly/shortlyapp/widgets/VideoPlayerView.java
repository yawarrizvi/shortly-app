package com.shortly.shortlyapp.widgets;

/**
 * Created by bashirahmad on 20/05/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shortly.shortlyapp.Logic.ProgressHandler.ProgressHandler;
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.tasks.GetVideoFirstFrameTask;

import java.util.Formatter;
import java.util.Locale;



/**
 * Created by Bashir on 4/15/16.
 */
public class VideoPlayerView extends RelativeLayout implements View.OnClickListener, SurfaceHolder.Callback, GetVideoFirstFrameTask.GetFirstFrameListener {

    private static final String TAG = "VideoPlayerView";
    private static final int DEFAULT_SHOW_TIME = 5000;
    private MediaPlayer mPlayer;
    //private PowerManager powerManager = null;
    //private PowerManager.WakeLock wakeLock = null;
    private SurfaceView mSurfaceView;
    private SurfaceHolder surfaceHolder;
    // view
    private ImageButton mBtnPlay, mBtnPause;
    private TextView mTxtCurrentTime;
    private TextView mTxtTotalTime;
    private SeekBar mSeekBar;
    private RelativeLayout mLayoutController;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private boolean mControllerIsShowing;
    private boolean mIsDragging;
    private boolean isPrepared = false;
    private int initialTime = 0;
    private Activity mActivity;

    private int mWidth;
    private int mHeight;

    private OnVideoProgressChangedListener mOnVideoProgressChangedListener;

    private String mVideoPath;

    private Activity parentActivity;

    private int mCurrentTime = 0;
    private int mDuration = 0;
    private Handler mHandler = new Handler();
    Runnable mVideoProgressChangeRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "mVideoProgressChangeRunnable run");
            if (mPlayer != null && mPlayer.isPlaying()) {
                int position = updateProgress();
                Log.d(TAG, "position: " + position);
                if (mOnVideoProgressChangedListener != null) {
                    mOnVideoProgressChangedListener.onVideoProgressChanged(position / 1000);
                }
                mHandler.postDelayed(this, 1000);
            } else {
                mHandler.removeCallbacks(this);
            }
        }
    };
    Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.d(TAG, "mShowRunnable run");
            updateProgress();
            mHandler.postDelayed(this, 1000);
        }
    };
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.d(TAG, "mHideRunnable hide controller");
            mHandler.removeCallbacks(mShowRunnable);
//            mLayoutController.setVisibility(View.GONE);
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    Log.d(TAG, "pause button set to gone");
                    mBtnPause.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, "pause button set to visible");
                    if (mBtnPlay.getVisibility() != View.VISIBLE) {
                        mBtnPause.setVisibility(View.VISIBLE);
                    }
                }
            }
            mControllerIsShowing = false;
        }
    };

    // init seekBar change listener
    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            showController(3600000);
            mIsDragging = true;
            mHandler.removeCallbacks(mShowRunnable);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
            Log.d(TAG, "onProgressChanged entry");
            if (mPlayer == null) {
                return;
            }

            long newPosition = (long) (mDuration * (progress * 1.0f / 100));
            Log.d(TAG, "newPosition: " + newPosition + " fromUser:" + fromUser + " mDuration:" + mDuration);
            if (!fromUser) {
                if (newPosition >= mDuration) {
                    stop();
                }
                return;
            }

            if (newPosition > mDuration) {
                newPosition = mDuration;
                if (mPlayer.isPlaying()) {
                    stop();
                }
            }

            Log.d(TAG, "seek to");
            mPlayer.seekTo((int) newPosition);

            if (mTxtCurrentTime != null) {
                Log.d(TAG, "set current time");
                mTxtCurrentTime.setText(stringForTime((int) newPosition));
            }
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mIsDragging = false;
            updateProgress();
            mHandler.post(mShowRunnable);
        }
    };

    public VideoPlayerView(Context context) {
        super(context);
        initView();
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * show video controller
     */
    public void showController() {
        showController(DEFAULT_SHOW_TIME);
    }

    /**
     * init views by id
     */
    private void initView() {
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.layout_video_player, null);

        this.addView(view);
        initControllerView(view);
        //powerManager = (PowerManager) getContext().getSystemService(Service.POWER_SERVICE);
        //wakeLock = this.powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Lock");
        //wakeLock.setReferenceCounted(false);
    }

    /**
     * init video controller view
     *
     * @param view
     */
    private void initControllerView(View view) {
        mBtnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        mBtnPlay.setOnClickListener(this);
        mBtnPause = (ImageButton) view.findViewById(R.id.btnPause);
        mBtnPause.setOnClickListener(this);
        mTxtCurrentTime = (TextView) view.findViewById(R.id.txtCurrentTime);
        mTxtTotalTime = (TextView) view.findViewById(R.id.txtTotalTime);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBarController);
        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(mSeekListener);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.videoSurface);
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(this);

        mSurfaceView.setOnClickListener(this);
        mLayoutController = (RelativeLayout) view.findViewById(R.id.layoutController);
//        mLayoutController.setVisibility(View.GONE);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    /**
     * load video by path
     *
     * @param videoPath
     */
    public void setVideoPath(String videoPath, boolean needFirstFrame, Activity activity) {
        mVideoPath = videoPath;

        if (needFirstFrame) {
            new GetVideoFirstFrameTask(videoPath, this).execute();
            if (null != mSurfaceView) {
                mSurfaceView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        // reset MediaPlayer
        mPlayer = new MediaPlayer();
        mCurrentTime = 0;
        mDuration = 0;
        // reset play and pause button and other view
        mBtnPlay.setVisibility(View.VISIBLE);
        mBtnPause.setVisibility(View.GONE);
//        mLayoutController.setVisibility(View.GONE);
        mSeekBar.setOnSeekBarChangeListener(mSeekListener);

        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // MediaPlayer prepare listener
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    isPrepared = true;
                    setCurrentTime(initialTime);
                    //if(mSurfaceView.is)
                    try {
                        mPlayer.setDisplay(mSurfaceView.getHolder());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onPrepared entry");
                    //setCurrentTime(0);
                    setDuration(mPlayer.getDuration());
                    mTxtCurrentTime.setText(stringForTime(0));
                    mTxtTotalTime.setText(stringForTime(mDuration));
                    // show video controller
                    showController(DEFAULT_SHOW_TIME);
                }
            });

            // completion
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    Log.d(TAG, "onCompletion entry");
                    mPlayer.seekTo(1);
                    mBtnPause.setVisibility(View.VISIBLE);
                    mBtnPause.setImageResource(R.mipmap.btn_play_white);
                    if (mTxtCurrentTime != null) {
                        mTxtCurrentTime.setText(stringForTime(0));
                    }

//                    if (getParentActivity() != null && getParentActivity() instanceof VideoPlayActivity) {
//                        getParentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "setVideoPath Exception");
        }
    }

    public void prepare() {

        playVideo(mActivity);

        this.postDelayed(new Runnable() {
            @Override
            public void run() {

                pauseVideo();
            }
        }, 200);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlay:
                playVideo(mActivity);
                break;
            case R.id.btnPause:
                pauseVideo();
                break;
            case R.id.videoSurface:
                Log.d(TAG, "onclick video surface");
                if (!mControllerIsShowing) {
                    showController(DEFAULT_SHOW_TIME);
                }
                break;
        }
    }

    public void playVideo(Activity activity) {
        Log.d(TAG, "onclick play ");
        if (mActivity == null) {
            if (activity != null) {
                mActivity = activity;
            }
        }
        if (null == mPlayer) {
            return;
        }
        play();
        mBtnPlay.setVisibility(View.GONE);
        mBtnPause.setVisibility(View.VISIBLE);
        mBtnPause.setImageResource(R.mipmap.btn_pause_white);
        mTxtCurrentTime.setText(stringForTime(0));
    }

    public void playVideoFromPosition(){

    }

    public void pauseVideo() {

        Log.d(TAG, "onclick pause ");
        if (null == mPlayer) {
            return;
        }
        if (mPlayer.isPlaying()) {
            Log.d(TAG, "onclick to pause");
            pause();
            mBtnPause.setImageResource(R.mipmap.btn_play_white);
            mHandler.post(mShowRunnable);
            mHandler.removeCallbacks(mVideoProgressChangeRunnable);
        } else {
            Log.d(TAG, "onclick to play");
            start();
            mBtnPause.setImageResource(R.mipmap.btn_pause_white);
            mHandler.removeCallbacks(mShowRunnable);
            mHandler.post(mVideoProgressChangeRunnable);
            showController(DEFAULT_SHOW_TIME);
        }

    }

    /**
     * @return controller show or not
     */
    public boolean isControllerShowing() {
        return mControllerIsShowing;
    }

    /**
     * show video controller
     *
     * @param timeout
     */
    public void showController(int timeout) {
        Log.d(TAG, "showController: " + mControllerIsShowing);
        if (!mControllerIsShowing) {
            mLayoutController.setVisibility(View.VISIBLE);
            if (mBtnPlay.getVisibility() != View.VISIBLE) {
                mBtnPause.setVisibility(View.VISIBLE);
            }
            if (mPlayer == null) {
                return;
            }
            if (mPlayer.isPlaying()) {
                Log.d(TAG, "isPlaying");
                mHandler.removeCallbacks(mShowRunnable);
                mHandler.post(mShowRunnable);
            } else {
                mHandler.removeCallbacks(mShowRunnable);
            }
            mControllerIsShowing = true;
            if (timeout != 0) {
                mHandler.removeCallbacks(mHideRunnable);
                mHandler.postDelayed(mHideRunnable, timeout);
            }
        }
        ProgressHandler.hideProgressDialogue();
    }

    /**
     * show time formatter 00:00
     *
     * @param timeMs
     * @return
     */
    private String stringForTime(int timeMs) {
        String result;
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            result = mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            result = mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
        Log.d(TAG, "stringForTime: " + result);
        return result;
    }

    /**
     * update progress bar
     *
     * @return current time
     */
    private int updateProgress() {
        Log.d(TAG, "updateProgress entry");
        if (mPlayer == null || mIsDragging) {
            return 0;
        }

        int position = mPlayer.getCurrentPosition();
        Log.d(TAG, "updateProgress position: " + position);
        if (mSeekBar != null) {
            if (mDuration > 0) {
                // use long to avoid overflow
                int pos = (int) (100 * (position * 1.0f / mDuration));
                mSeekBar.setProgress(pos);
            }
        }

        if (mTxtCurrentTime != null) {
            Log.d(TAG, "updateProgress position: " + position);
            mTxtCurrentTime.setText(stringForTime(position));
        }

        return position;
    }

    public Activity getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }


    /**
     * play video
     * set video url
     * prepare video
     */
    public void play() {
        if (mActivity != null) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Activity activity = getParentActivity();
        if (activity != null) {
            Window window = activity.getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
        //wakeLock.acquire();
        try {
            mSurfaceView.setBackgroundColor(Color.TRANSPARENT);
            mPlayer.setDataSource(getContext(), Uri.parse(mVideoPath));
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler.post(mShowRunnable);
        mHandler.post(mVideoProgressChangeRunnable);
        showController(DEFAULT_SHOW_TIME);
    }

    /**
     * start and pause video
     */
    public void start() {
//        if (getParentActivity() != null && getParentActivity() instanceof VideoPlayActivity
//                && getParentActivity().getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            getParentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        mPlayer.start();
    }

    /**
     * start and pause video
     */
    public void pause() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            } else {
                mPlayer.start();
            }
        }
        showController(DEFAULT_SHOW_TIME);
        Activity activity = getParentActivity();
        if (activity != null) {
            Window window = activity.getWindow();
            if (window != null) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
//        wakeLock.release();
    }

    public void stop() {
        Log.d(TAG, "stop entry");
        if (mPlayer == null) {
            return;
        }
        // reset player
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;

        // reset views
        mBtnPlay.setVisibility(View.GONE);
        mBtnPause.setVisibility(View.GONE);
        Activity activity = getParentActivity();
        if (activity != null) {
            Window window = activity.getWindow();
            if (window != null) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
//        wakeLock.release();
        mSeekBar.setOnSeekBarChangeListener(null);
        mSeekBar.setProgress(0);
        mControllerIsShowing = false;
        // remove callback
        mHandler.removeCallbacks(mShowRunnable);
        mHandler.removeCallbacks(mVideoProgressChangeRunnable);
        mHandler.removeCallbacks(mHideRunnable);
    }

    /**
     * set video cover image
     *
     * @param bitmap
     */
    public void setVideoCover(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        mSurfaceView.setBackgroundDrawable(drawable);
        ViewGroup.LayoutParams params = VideoPlayerView.this.getLayoutParams();
        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = (int) (mWidth * 1.0f / bitmap.getWidth() * bitmap.getHeight());
        params.width = mWidth;
        params.height = mHeight;
        setLayoutParams(params);
    }

    /**
     * set video cover image
     *
     * @param drawable
     */
    public void setVideoCover(Drawable drawable) {
        mSurfaceView.setBackgroundDrawable(drawable);
        ViewGroup.LayoutParams params = VideoPlayerView.this.getLayoutParams();
        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = (int) (mWidth * 1.0f / drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight());
        params.width = mWidth;
        params.height = mHeight;
        setLayoutParams(params);
    }

    /**
     * get video current time
     *
     * @return
     */
    public int getCurrentTime() {

        return mPlayer.getCurrentPosition();
    }

    /**
     * set video current time
     *
     * @param current
     */
    public void setCurrentTime(int current) {

        if (isPrepared&& mPlayer!=null) {
            this.mCurrentTime = current;

            Log.e(TAG, "Current Time is seconds" + current);

            mPlayer.seekTo(current);

            String timeString = stringForTime(current);

            Log.e(TAG, "Time from String" + timeString);

            mTxtCurrentTime.setText(timeString);
        } else {

            initialTime = current;

        }
    }

    /**
     * get video duration
     *
     * @return video duration
     */
    public int getDuration() {
        return mDuration;
    }

    /**
     * set video duration
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.e(TAG,"Surface created ");

        if (null != mPlayer) {
            mPlayer.setDisplay(mSurfaceView.getHolder());
        } else {
            setVideoPath(mVideoPath, false, null);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != mPlayer) {
            stop();
        }
    }

    public boolean isPlaying() {
        if (null == mPlayer) {
            return false;
        }
        return mPlayer.isPlaying();
    }

    public int getViewWidth() {
        return mWidth;
    }

    public int getViewHeight() {
        return mHeight;
    }

    public void setOnVideoProgressChangedListener(OnVideoProgressChangedListener listener) {
        mOnVideoProgressChangedListener = listener;
    }

    @Override
    public void onGetFirstFrameComplete(Bitmap bitmap) {
        if ((null != mSurfaceView) && (mBtnPlay.getVisibility() == View.VISIBLE)) {
            setVideoCover(bitmap);
        }
    }


    public interface OnVideoProgressChangedListener {
        /**
         * @param position second
         */
        void onVideoProgressChanged(int position);
    }
}
