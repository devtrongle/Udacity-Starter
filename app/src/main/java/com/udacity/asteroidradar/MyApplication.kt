package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.logs.DebugLogTree
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(DebugLogTree())

        CoroutineScope(Dispatchers.Default).launch {
            Timber.d("applicationScope.launch  start")
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .build()

            val periodicWork = PeriodicWorkRequestBuilder<DownloadDataWork>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this@MyApplication)
                .enqueueUniquePeriodicWork(
                    DownloadDataWork.WORK_NAME,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    periodicWork
                )
        }
    }
}