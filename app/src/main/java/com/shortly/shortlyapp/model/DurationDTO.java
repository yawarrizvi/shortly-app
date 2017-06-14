package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 14/06/2017.
 */

public class DurationDTO {
    private Meta meta;
    private List<DurationResponse> response = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<DurationResponse> getResponse() {
        return response;
    }

    public void setResponse(List<DurationResponse> response) {
        this.response = response;
    }

}
