package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class VideoDetailDTO {
    private Meta meta;
    private List<VideoDetailResponse> response = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<VideoDetailResponse> getResponse() {
        return response;
    }

    public void setResponse(List<VideoDetailResponse> response) {
        this.response = response;
    }
}
