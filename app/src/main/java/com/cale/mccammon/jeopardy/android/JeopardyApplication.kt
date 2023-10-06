package com.cale.mccammon.jeopardy.android

import android.app.Application
import com.cale.mccammon.jeopardy.module.domain.JeopardyComponent
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class JeopardyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}