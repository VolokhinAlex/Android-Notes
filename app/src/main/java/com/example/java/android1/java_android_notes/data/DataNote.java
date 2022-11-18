package com.example.java.android1.java_android_notes.data;

import android.os.Parcel;
import android.os.Parcelable;

public class DataNote implements Parcelable {

    private String mId;
    private String mNoteName;
    private String mNoteDescription;
    private String mNoteDate;

    public DataNote(String mNoteName, String mNoteDescription, String mNoteDate) {
        this.mNoteName = mNoteName;
        this.mNoteDescription = mNoteDescription;
        this.mNoteDate = mNoteDate;
    }

    protected DataNote(Parcel in) {
        mNoteName = in.readString();
        mNoteDescription = in.readString();
        mNoteDate = in.readString();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public static final Creator<DataNote> CREATOR = new Creator<DataNote>() {
        @Override
        public DataNote createFromParcel(Parcel in) {
            return new DataNote(in);
        }

        @Override
        public DataNote[] newArray(int size) {
            return new DataNote[size];
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
    }
}
