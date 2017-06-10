package com.shortly.shortlyapp.api;

/**
 * Created by yarizvi on 06/06/2017.
 */

public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}


