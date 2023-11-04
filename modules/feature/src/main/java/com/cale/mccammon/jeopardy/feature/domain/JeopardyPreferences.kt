package com.cale.mccammon.jeopardy.feature.domain

import android.app.Application
import android.content.Context
import javax.inject.Inject

interface JeopardyPreferences {
    fun putInt(key: String, value: Int)
    fun getInt(key: String, value: Int): Int
    fun putString(key: String, value: String)
    fun getString(key: String, value: String): String
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

    override fun getInt(key: String, value: Int): Int {
        return preferences.getInt(key, value)
    }

    override fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String, value: String): String {
        return preferences.getString(key, value) ?: value
    }
}