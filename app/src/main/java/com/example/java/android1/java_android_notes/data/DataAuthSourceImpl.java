package com.example.java.android1.java_android_notes.data;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataAuthSourceImpl implements DataAuthSource {

    private static final Object LOCK_KEY = new Object();
    private volatile static DataAuthSourceImpl sInstance;
    private final LinkedList<DataAuth> mDataAuth = new LinkedList<>();

    public static DataAuthSourceImpl getInstance() {
        DataAuthSourceImpl instance = sInstance;
        if (instance == null) {
            synchronized (LOCK_KEY) {
                if (sInstance == null) {
                    instance = new DataAuthSourceImpl();
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    @Override
    public List<DataAuth> getDataAuth() {
        return Collections.unmodifiableList(mDataAuth);
    }

    @Override
    public void createItem(@NonNull DataAuth dataAuth) {
        mDataAuth.add(dataAuth);
    }

    @Override
    public void clear() {
        mDataAuth.clear();
    }

}
