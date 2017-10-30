package io.cosgrove.nowplaying_history.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vivalldi on 10/27/17.
 */

public class SongHistory implements Parcelable {

    private String mSongTitle;
    private String mSongArtist;
    private String mSongHeardDate;

    public SongHistory(String songTitle, String songArtist, String songHeardDate) {
        mSongTitle = songTitle;
        mSongArtist = songArtist;
        mSongHeardDate = songHeardDate;
    }

    public SongHistory(String titleFromNotification, String songHeardDate) {
        final String regex = "(?<title>.*?)\\ by (?<artist>.*)";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(titleFromNotification);

        matcher.find();

        if (matcher.groupCount() >= 2) {
            mSongTitle = matcher.group("title");
            mSongArtist = matcher.group("artist");
            mSongHeardDate = songHeardDate;
        }

    }

    public String getSongTitle() {
        return mSongTitle;
    }

    public void setSongTitle(String songTitle) {
        mSongTitle = songTitle;
    }

    public String getSongArtist() {
        return mSongArtist;
    }

    public void setSongArtist(String songArtist) {
        mSongArtist = songArtist;
    }

    public String getSongHeardDate() {
        return mSongHeardDate;
    }

    public void setSongHeardDate(String songHeardDate) {
        mSongHeardDate = songHeardDate;
    }

    private SongHistory(Parcel in) {
        mSongTitle = in.readString();
        mSongArtist = in.readString();
        mSongHeardDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSongTitle);
        dest.writeString(mSongArtist);
        dest.writeSerializable(mSongHeardDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SongHistory> CREATOR = new Parcelable.Creator<SongHistory>() {
        @Override
        public SongHistory createFromParcel(Parcel in) {
            return new SongHistory(in);
        }

        @Override
        public SongHistory[] newArray(int size) {
            return new SongHistory[size];
        }
    };
}
