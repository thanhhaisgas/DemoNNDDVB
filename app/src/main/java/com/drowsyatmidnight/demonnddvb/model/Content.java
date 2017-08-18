package com.drowsyatmidnight.demonnddvb.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by haint on 16/08/2017.
 */

public class Content implements Parcelable {
    private String imgURL;
    private String http;

    public Content(String imgURL, String http) {
        this.imgURL = imgURL;
        this.http = http;
    }

    protected Content(Parcel in) {
        imgURL = in.readString();
        http = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgURL);
        dest.writeString(http);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }

    @Override
    public String toString() {
        return "Content{" +
                "imgURL='" + imgURL + '\'' +
                ", http='" + http + '\'' +
                '}';
    }
}
