package com.drowsyatmidnight.demonnddvb.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by haint on 16/08/2017.
 */

public class Article implements Parcelable{
    private String title;
    private Content description;

    protected Article(Parcel in) {
        title = in.readString();
        description = in.readParcelable(Content.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeParcelable(description, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Content getDescription() {
        return description;
    }

    public void setDescription(Content description) {
        this.description = description;
    }

    public Article(String title, Content description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", description=" + description.toString() +
                '}';
    }
}
