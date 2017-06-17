package com.shortly.shortlyapp.model;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class LoginResponse {

    private Integer userId;
    private Object type;
    private double gender;
    private Object fullName;
    private Object country;
    private Object city;
    private Object address;
    private Object phone;
    private String email;
    private Object image;
    private Integer autoPlay;
    private Integer videoPrivacy;
    private String created_at;
    private String updated_at;
    private Integer status;
    private String api_token;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public double getGender() {
        return gender;
    }

    public void setGender(double gender) {
        this.gender = gender;
    }

    public String getFullName() {
        if (fullName == null){
            return "";
        }
        else {
            return (String) fullName;
        }
    }

    public void setFullName(Object fullName) {
        this.fullName = fullName;
    }

    public Object getCountry() {
        return country;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public String getCity() {
        if (city == null) {
            return "";
        } else {
            return (String) city;
        }
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public String getPhone() {
        if (phone == null) {
            return "";
        } else {
            return (String) phone;
        }
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public Integer getAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(Integer autoPlay) {
        this.autoPlay = autoPlay;
    }

    public Integer getVideoPrivacy() {
        return videoPrivacy;
    }

    public void setVideoPrivacy(Integer videoPrivacy) {
        this.videoPrivacy = videoPrivacy;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }
}
