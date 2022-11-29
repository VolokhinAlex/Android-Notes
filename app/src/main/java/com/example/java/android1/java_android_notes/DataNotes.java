package com.example.java.android1.java_android_notes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DataNotes implements Parcelable {

    private String mNoteName;
    private String mNoteDescription;
    private String mNoteDate;
    private int mCurrentPosition;

    public DataNotes(int mCurrentPosition, String mNoteName, String mNoteDescription, String mNoteDate) {
        this.mCurrentPosition = mCurrentPosition;
        this.mNoteName = mNoteName;
        this.mNoteDescription = mNoteDescription;
        this.mNoteDate = mNoteDate;
    }

    protected DataNotes(Parcel in) {
        mNoteName = in.readString();
        mNoteDescription = in.readString();
        mNoteDate = in.readString();
        mCurrentPosition = in.readInt();
    }

    public static final Creator<DataNotes> CREATOR = new Creator<DataNotes>() {
        @Override
        public DataNotes createFromParcel(Parcel in) {
            return new DataNotes(in);
        }

        @Override
        public DataNotes[] newArray(int size) {
            return new DataNotes[size];
        }
    };

    public String getNoteName() {
        return mNoteName;
    }

    public String getNoteDescription() {
        return mNoteDescription;
    }

    public String getNoteDate() {
        return mNoteDate;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setNoteName(String mNoteName) {
        this.mNoteName = mNoteName;
    }

    public void setNoteDescription(String mNoteDescription) {
        this.mNoteDescription = mNoteDescription;
    }

    public void setNoteDate(String mNoteDate) {
        this.mNoteDate = mNoteDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mNoteName);
        parcel.writeString(mNoteDescription);
        parcel.writeString(mNoteDate);
        parcel.writeInt(mCurrentPosition);
    }
}
