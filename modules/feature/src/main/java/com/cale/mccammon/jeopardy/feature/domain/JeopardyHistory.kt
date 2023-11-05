package com.cale.mccammon.jeopardy.feature.domain

import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyHistoryItem
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

interface JeopardyHistory {
    fun add(item: JeopardyHistoryItem)
    fun get(): List<JeopardyHistoryItem>
}

class JeopardyHistoryImpl @Inject constructor(
    private val preferences: JeopardyPreferences
): JeopardyHistory {

    companion object {
        private const val HISTORY_PREF_KEY = "JEOPARDY_HISTORY_PREF_KEY"
    }

    private data class Data(
        @SerializedName("items")
        val items: List<JeopardyHistoryItem>
    )

    private val gson by lazy {
        Gson().newBuilder().create()
    }

    override fun add(item: JeopardyHistoryItem) {
        val list = get().toMutableList()
        list.add(0, item)
        preferences.putString(
            HISTORY_PREF_KEY,
            gson.toJson(
                Data(list),
                Data::class.java
            ).toString()
        )
    }

    override fun get(): List<JeopardyHistoryItem> {
        return gson.fromJson(
            preferences.getString(HISTORY_PREF_KEY, "{\"items\":[]}"),
            Data::class.java
        ).items
    }

}
