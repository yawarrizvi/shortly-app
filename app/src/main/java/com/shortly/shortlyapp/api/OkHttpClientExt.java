package com.shortly.shortlyapp.api;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by yarizvi on 06/06/2017.
 */

public class OkHttpClientExt extends OkHttpClient {
    public static final Object TAG_CALL = new Object();

    @Override
    public Call newCall(Request request) {
        Request.Builder requestBuilder = request.newBuilder();
        requestBuilder.tag(TAG_CALL);
        return super.newCall(requestBuilder.build());
    }
}
