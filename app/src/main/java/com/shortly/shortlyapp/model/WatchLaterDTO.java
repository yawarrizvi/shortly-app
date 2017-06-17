package com.shortly.shortlyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yarizvi on 07/06/2017.
 */

public class WatchLaterDTO implements Parcelable {
    private Meta meta;
    private List<WatchLaterResponse> response = null;

    public WatchLaterDTO(){}

    protected WatchLaterDTO(Parcel in) {
        response = in.createTypedArrayList(WatchLaterResponse.CREATOR);
    }


    public static final Creator<WatchLaterDTO> CREATOR = new Creator<WatchLaterDTO>() {
        @Override
        public WatchLaterDTO createFromParcel(Parcel in) {
            return new WatchLaterDTO(in);
        }

        @Override
        public WatchLaterDTO[] newArray(int size) {
            return new WatchLaterDTO[size];
        }
    };

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<WatchLaterResponse> getResponse() {
        return response;
    }

    public void setResponse(List<WatchLaterResponse> response) {
        this.response = response;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(response);
    }
}
