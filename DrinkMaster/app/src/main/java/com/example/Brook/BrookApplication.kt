package com.example.Brook


import android.app.Application
import android.content.Context

class BrookApplication :Application() {

    object Globals {
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        Globals.appContext = applicationContext
    }

}