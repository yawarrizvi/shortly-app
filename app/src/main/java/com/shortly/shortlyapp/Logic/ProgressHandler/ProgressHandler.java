package com.shortly.shortlyapp.Logic.ProgressHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shortly.shortlyapp.Interfaces.ProgressLoaderInterface;
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.utils.Constants;

/**
 * Created by yarizvi on 10/06/2017.
 */

public class ProgressHandler {

    private static ProgressHandler mInstance;
    private static Context mContext;
    private static ProgressDialog mProgressDialog;

    private static fr.castorflex.android.circularprogressbar.CircularProgressBar mSmoothProgressBar;
    private static TextView mTitleTextView;
    private static TextView mMessageTextView;
    private static Button mPositiveButton;
    private static Button mNegativeButton;
    private static Handler mHandler;
    private static int mProgressBarType;
    private static ProgressLoaderInterface mProgLoaderInterface;


    public static void showProgressDialog(Context context, String title, String message, int progress, int progressBarStyle, String positiveButtonTitle, String negativeButtonTitle) {

        if (mHandler != null) {
            mHandler = null;
        }
        mHandler = new Handler(context.getMainLooper());

        if (mProgressDialog != null) {
            mProgressDialog = null;
            mTitleTextView = null;
            mMessageTextView = null;
            mPositiveButton = null;
            mNegativeButton = null;
        }

        View view = (View) LayoutInflater.from(context).inflate(R.layout.view_pogress_dialogue, null);

        mSmoothProgressBar = (fr.castorflex.android.circularprogressbar.CircularProgressBar) view.findViewById(R.id.smoothProgressBar);
        mTitleTextView = (TextView) view.findViewById(R.id.progress_dialogue_title);
        mMessageTextView = (TextView) view.findViewById(R.id.progress_dialogue_message);
        mPositiveButton = (Button) view.findViewById(R.id.progress_dialogue_button_positve);
        mNegativeButton = (Button) view.findViewById(R.id.progress_dialogue_button_negative);

        mProgressDialog = new ProgressDialog(context);
        mTitleTextView.setText(title);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);

        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgressDialogue();
            }
        });

        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgressDialogue();
            }
        });

        setDialogButtons(positiveButtonTitle, negativeButtonTitle);
        setProgressAndMessage(progressBarStyle, message, progress);

        mProgressDialog.getWindow().setLayout(view.getWidth(), view.getHeight());
        mProgressDialog.show();
        mProgressDialog.setContentView(view);

    }

    public static void upDateProgressDialog(Context context, final String title, final String message, final int progress, final int progressBarStyle, final String positiveButtonTitle, final String negativeButtonTitle) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            Runnable changeMessage = new Runnable() {
                @Override
                public void run() {
                    String progressMessage = message;
                    setDialogButtons(positiveButtonTitle, negativeButtonTitle);
                    setProgressAndMessage(progressBarStyle, message, progress);
                }
            };
            mHandler.post(changeMessage);
        }
    }

    public static void hideProgressDialogue() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private static void setDialogButtons(String positiveButtonTitle, String negativeButtonTitle) {


        if (!positiveButtonTitle.isEmpty()) {
            mPositiveButton.setVisibility(View.VISIBLE);

        } else {
            mPositiveButton.setVisibility(View.GONE);
        }
        mPositiveButton.setText(positiveButtonTitle);
        if (!negativeButtonTitle.isEmpty()) {
            mNegativeButton.setVisibility(View.VISIBLE);
        } else {
            mNegativeButton.setVisibility(View.GONE);

        }
        mNegativeButton.setText(negativeButtonTitle);

    }

    private static void setProgressAndMessage(int progressBarStyle, String message, int progress) {
        String progressMessage = message;
        switch (progressBarStyle) {
            case Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED:
                mSmoothProgressBar.setVisibility(View.VISIBLE);
                break;
        }
        mMessageTextView.setText(progressMessage);
    }

    public static void setProgLoaderInterface(ProgressLoaderInterface syncInterface) {
        if (mProgLoaderInterface != null) {
            mProgLoaderInterface = null;
        }
        mProgLoaderInterface = syncInterface;
    }
}
