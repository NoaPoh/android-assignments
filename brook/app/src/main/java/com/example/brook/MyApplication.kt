package com.example.brook

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = getApplicationContext()
    }

    companion object {
        private var context: Context? = null
        fun getMyContext(): Context? {
            return context
        }
    }
}
