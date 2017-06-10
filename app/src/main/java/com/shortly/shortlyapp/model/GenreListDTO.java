package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class GenreListDTO {
    private Meta meta;
    private List<GenreListResponse> response = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<GenreListResponse> getResponse() {
        return response;
    }

    public void setResponse(List<GenreListResponse> response) {
        this.response = response;
    }

}
