package com.example.java.android1.java_android_notes.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface DataAuthSource {

    List<DataAuth> getDataAuth();
    void createItem(@NonNull DataAuth dataAuth);
    void clear();

}
