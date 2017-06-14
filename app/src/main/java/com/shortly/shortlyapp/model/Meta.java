package com.shortly.shortlyapp.model;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class Meta {
    private Integer status;
    private String message;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
