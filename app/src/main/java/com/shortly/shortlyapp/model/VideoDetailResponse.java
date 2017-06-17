package com.shortly.shortlyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class VideoDetailResponse implements Parcelable {
    private Integer videoId;
    private Integer userId;
    private Object categoryId;
    private String title;
    private String description;
    private String tags;
    private String casts;
    private Object format;
    private String duration;
    private String path;
    private Integer license;
    private Integer isFeatured;
    private Integer type;
    private Object dateTime;
    private Integer status;
    private String created_at;
    private String updated_at;
    private Integer viewsCount;
    private Integer totalVideos;
    private Boolean liked;
    private int later;
    private Boolean playlist;
    private String category;
    private String thumbnails;
    private int time;

    public VideoDetailResponse(){

    }

    protected VideoDetailResponse(Parcel in) {
        videoId = in.readInt();
        casts = in.readString();
        title = in.readString();
        path = in.readString();
    }

    public static final Creator<VideoDetailResponse> CREATOR = new Creator<VideoDetailResponse>() {
        @Override
        public VideoDetailResponse createFromParcel(Parcel in) {
            return new VideoDetailResponse(in);
        }

        @Override
        public VideoDetailResponse[] newArray(int size) {
            return new VideoDetailResponse[size];
        }
    };

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Integer getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(Integer totalVideos) {
        this.totalVideos = totalVideos;
    }

    public Boolean getLiked() {
        if (liked == null) {
            return false;
        }
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public int getLater() {
        return later;
    }

    public void setLater(int later) {
        this.later = later;
    }

    public Boolean getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Boolean playlist) {
        this.playlist = playlist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    /*public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/
}
