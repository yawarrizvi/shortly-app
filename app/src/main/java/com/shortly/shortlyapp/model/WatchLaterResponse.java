package com.shortly.shortlyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class WatchLaterResponse implements Parcelable {
    private Integer videoId;
    private Integer userId;
    private Object categoryId;
    private String title;
    private String description;
    private String tags;
    private String casts;
    private Object format;
    private Integer duration;
    private String thumbnails;
    private String path;
    private Integer license;
    private Integer isFeatured;
    private Integer type;
    private Object dateTime;
    private Integer status;
    private String createdAt;
    private String updatedAt;
    private String jobId;
    private String videoLink;
    private Object allowCountry;
    //TODO: update boolean to int for later
    private int later;
    private Integer time;

    public WatchLaterResponse(){}

    protected WatchLaterResponse(Parcel in) {
        videoId = in.readInt();
        title = in.readString();
        casts = in.readString();
        path = in.readString();
    }

    public static final Creator<WatchLaterResponse> CREATOR = new Creator<WatchLaterResponse>() {
        @Override
        public WatchLaterResponse createFromParcel(Parcel in) {
            return new WatchLaterResponse(in);
        }

        @Override
        public WatchLaterResponse[] newArray(int size) {
            return new WatchLaterResponse[size];
        }
    };

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Object categoryId) {
        this.categoryId = categoryId;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCasts() {
        return casts;
    }

    public void setCasts(String casts) {
        this.casts = casts;
    }

    public Object getFormat() {
        return format;
    }

    public void setFormat(Object format) {
        this.format = format;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLicense() {
        return license;
    }

    public void setLicense(Integer license) {
        this.license = license;
    }

    public Integer getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Integer isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getDateTime() {
        return dateTime;
    }

    public void setDateTime(Object dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public Object getAllowCountry() {
        return allowCountry;
    }

    public void setAllowCountry(Object allowCountry) {
        this.allowCountry = allowCountry;
    }

    public int getLater() {
        return later;
    }

    public void setLater(int later) {
        this.later = later;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(videoId);
        parcel.writeString(title);
        parcel.writeString(casts);
        parcel.writeString(path);
    }
}
