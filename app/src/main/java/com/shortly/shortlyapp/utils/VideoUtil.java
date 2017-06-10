package com.shortly.shortlyapp.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import java.io.File;

//import com.coremedia.iso.boxes.Container;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

/**
 * Created by Bashir on 16/4/5.
 */
public class VideoUtil {

    private static final String TAG = "VideoUtil";



    public static Bitmap getFirstFrameUsingThumbnailUtil(String url){

        File file = new File(url);
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

        return bitmap;
    }


 }
