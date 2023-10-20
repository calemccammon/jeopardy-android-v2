package com.cale.mccammon.jeopardy.feature.domain

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

interface JeopardyPreferences {
    fun putInt(key: String, value: Int)
    fun getInt(key: String, default: Int): Int
}

class JeopardyPreferencesImpl @Inject constructor(
    application: Application
): JeopardyPreferences {
    private val preferences = application.getSharedPreferences(
        application.packageName + "JEOPARDY_SHARED_PREFERENCES",
        Context.MODE_PRIVATE
    )

    override fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    override fun getInt(key: String, default: Int): Int {
        return preferences.getInt(key, default)
    }
}