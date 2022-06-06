package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.logs.DebugLogTree
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG)
            Timber.plant(DebugLogTree())
    }
}