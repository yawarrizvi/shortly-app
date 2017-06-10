package com.shortly.shortlyapp.model;

import java.util.List;

/**
 * Created by yarizvi on 08/06/2017.
 */

public class ChannelResponse {
    private Integer channelId;
    private Integer userId;
    private String title;
    private String description;
    private String coverImage;
    private Object facebookUrl;
    private Object twitterUrl;
    private Object googlePlusUrl;
    private Integer type;
    private String createdAt;
    private String updatedAt;
    private Integer channelsubscribersCount;
    private List<Video> videos = null;
    private User user;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Object getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(Object facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public Object getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(Object twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public Object getGooglePlusUrl() {
        return googlePlusUrl;
    }

    public void setGooglePlusUrl(Object googlePlusUrl) {
        this.googlePlusUrl = googlePlusUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getChannelsubscribersCount() {
        return channelsubscribersCount;
    }

    public void setChannelsubscribersCount(Integer channelsubscribersCount) {
        this.channelsubscribersCount = channelsubscribersCount;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
