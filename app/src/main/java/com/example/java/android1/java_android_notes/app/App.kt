package com.example.java.android1.java_android_notes.app

import android.app.Application
import com.example.java.android1.java_android_notes.room.NotesDataBase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        NotesDataBase.create(this)
    }

    companion object {
        var appInstance: App? = null
    }

}