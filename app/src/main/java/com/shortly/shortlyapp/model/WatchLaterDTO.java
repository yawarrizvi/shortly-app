package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class WatchLaterDTO {
    private Meta meta;
    private List<WatchLaterResponse> response = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<WatchLaterResponse> getResponse() {
        return response;
    }

    public void setResponse(List<WatchLaterResponse> response) {
        this.response = response;
    }
}
