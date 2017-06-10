package com.shortly.shortlyapp.model;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class SignUpResponse {
    private Integer userId;
    private Object type;
    private Object gender;
    private Object fullName;
    private Object country;
    private Object city;
    private Object address;
    private Object phone;
    private String email;
    private Object image;
    private Integer autoPlay;
    private Integer videoPrivacy;
    private Integer isActive;
    private String createdAt;
    private String updatedAt;
    private Integer status;
    private String apiToken;

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

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getFullName() {
        return fullName;
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

    public Object getCity() {
        return city;
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

    public Object getPhone() {
        return phone;
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

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
