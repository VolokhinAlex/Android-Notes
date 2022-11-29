package com.example.java.android1.java_android_notes.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class DataAuth implements Parcelable {

    private String email;
    private String fullName;
    private Uri imageProfile;

    public DataAuth(String email, String fullName, Uri imageProfile) {
        this.email = email;
        this.fullName = fullName;
        this.imageProfile = imageProfile;
    }

    protected DataAuth(Parcel in) {
        email = in.readString();
        fullName = in.readString();
    }

    public static final Creator<DataAuth> CREATOR = new Creator<DataAuth>() {
        @Override
        public DataAuth createFromParcel(Parcel in) {
            return new DataAuth(in);
        }

        @Override
        public DataAuth[] newArray(int size) {
            return new DataAuth[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(fullName);
    }

    public Uri getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(Uri imageProfile) {
        this.imageProfile = imageProfile;
    }
}
