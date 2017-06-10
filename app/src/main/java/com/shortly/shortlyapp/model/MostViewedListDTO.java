package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class MostViewedListDTO {
    private Meta meta;
    private List<MostViewedListResponse> response = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<MostViewedListResponse> getResponse() {
        return response;
    }

    public void setResponse(List<MostViewedListResponse> response) {
        this.response = response;
    }
}
