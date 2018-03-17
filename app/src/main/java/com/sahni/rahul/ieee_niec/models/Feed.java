package com.sahni.rahul.ieee_niec.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sahni on 30-Sep-17.
 */

public class Feed implements Parcelable{

    private String feedTitle;
    private String feedImageUrl;
    private String feedDetails;
    private String registerUrl;

    public Feed() {
    }

    protected Feed(Parcel in) {
        feedTitle = in.readString();
        feedImageUrl = in.readString();
        feedDetails = in.readString();
        registerUrl = in.readString();
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(feedTitle);
        parcel.writeString(feedImageUrl);
        parcel.writeString(feedDetails);
        parcel.writeString(registerUrl);
    }


    public String getFeedImageUrl() {
        return feedImageUrl;
    }

    public void setFeedImageUrl(String feedImageUrl) {
        this.feedImageUrl = feedImageUrl;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public String getFeedDetails() {
        return feedDetails;
    }

    public void setFeedDetails(String feedDetails) {
        this.feedDetails = feedDetails;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }
}
