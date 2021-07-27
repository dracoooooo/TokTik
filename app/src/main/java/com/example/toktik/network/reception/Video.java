package com.example.toktik.network.reception;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class Video implements Parcelable {
    private String _id;
    private String feedurl;
    private String nickname;
    private String description;
    private int likecount;
    private String avatar;
    private String thumbnails;

    public Video(){}

    protected Video(Parcel in) {
        _id = in.readString();
        feedurl = in.readString();
        nickname = in.readString();
        description = in.readString();
        likecount = in.readInt();
        avatar = in.readString();
        thumbnails = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(feedurl);
        dest.writeString(nickname);
        dest.writeString(description);
        dest.writeInt(likecount);
        dest.writeString(avatar);
        dest.writeString(thumbnails);
    }
}
