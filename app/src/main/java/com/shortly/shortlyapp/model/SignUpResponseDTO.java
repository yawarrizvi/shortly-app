package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class SignUpResponseDTO {
    private Meta meta;
    private List<SignUpResponse> response = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<SignUpResponse> getResponse() {
        return response;
    }

    public void setResponse(List<SignUpResponse> response) {
        this.response = response;
    }
}
