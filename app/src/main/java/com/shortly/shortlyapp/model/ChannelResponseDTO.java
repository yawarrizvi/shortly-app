package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 08/06/2017.
 */

public class ChannelResponseDTO {

    private Meta meta;
    private List<ChannelResponse> response = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<ChannelResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ChannelResponse> response) {
        this.response = response;
    }
}
