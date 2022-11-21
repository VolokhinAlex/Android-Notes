package com.example.java.android1.java_android_notes.data;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class DataNote implements Parcelable {

    private String mId;
    private String mNoteName;
    private String mNoteDescription;
    private String mNoteDate;
    private boolean mNoteFavorite;

    public DataNote(String mNoteName, String mNoteDescription,  boolean mNoteFavorite, String mNoteDate) {
        this.mNoteName = mNoteName;
        this.mNoteDescription = mNoteDescription;
        this.mNoteFavorite = mNoteFavorite;
        this.mNoteDate = mNoteDate;
    }

    protected DataNote(Parcel in) {
        mNoteName = in.readString();
        mNoteDescription = in.readString();
        mNoteDate = in.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mNoteFavorite = in.readBoolean();
        }
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

    public boolean getNoteFavorite() {
        return mNoteFavorite;
    }

    public void setNoteFavorite(boolean mNoteFavorite) {
        this.mNoteFavorite = mNoteFavorite;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            parcel.writeBoolean(mNoteFavorite);
        }
    }

}
