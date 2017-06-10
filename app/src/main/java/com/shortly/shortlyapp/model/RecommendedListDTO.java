package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class RecommendedListDTO {
    private Meta meta;
    private List<RecommendedListResponse> response = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<RecommendedListResponse> getResponse() {
        return response;
    }

    public void setResponse(List<RecommendedListResponse> response) {
        this.response = response;
    }

}
