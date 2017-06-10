package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class DefaultDTO {
    private Meta meta;
    private List<DefaultResponse> response = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<DefaultResponse> getResponse() {
        return response;
    }

    public void setResponse(List<DefaultResponse> response) {
        this.response = response;
    }

}
