package com.example.java.android1.java_android_notes.data

import java.util.*

class DataAuthSourceImpl : DataAuthSource {
    private val mDataAuth = LinkedList<DataAuth>()
    override val dataAuth: List<DataAuth>
        get() = Collections.unmodifiableList(mDataAuth)

    override fun createItem(dataAuth: DataAuth) {
        mDataAuth.add(dataAuth)
    }

    override fun clear() {
        mDataAuth.clear()
    }

    companion object {
        private val LOCK_KEY = Any()

        @Volatile
        private var sInstance: DataAuthSourceImpl? = null
        @JvmStatic
        val instance: DataAuthSourceImpl?
            get() {
                var instance = sInstance
                if (instance == null) {
                    synchronized(LOCK_KEY) {
                        if (sInstance == null) {
                            instance = DataAuthSourceImpl()
                            sInstance = instance
                        }
                    }
                }
                return instance
            }
    }
}