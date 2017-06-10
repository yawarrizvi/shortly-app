package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class LoginResponseDTO {

    private Meta meta;
    private List<LoginResponse> response = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<LoginResponse> getResponse() {
        return response;
    }

    public void setResponse(List<LoginResponse> response) {
        this.response = response;
    }
}
