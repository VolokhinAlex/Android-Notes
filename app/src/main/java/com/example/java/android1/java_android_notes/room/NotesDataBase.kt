package com.example.java.android1.java_android_notes.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NotesDataBase : RoomDatabase() {

    abstract fun noteDao(): NotesDao

    companion object {
        private const val DB_NAME = "Notes.db"
        private var instance: NotesDataBase? = null

        fun getInstance() = instance
            ?: throw RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context?) {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context!!, NotesDataBase::class.java, DB_NAME).build()
            }
        }
    }

}