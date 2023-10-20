package com.cale.mccammon.jeopardy.feature.domain

import javax.inject.Inject

interface JeopardyScore {
    fun add(value: Int)
    fun subtract(value: Int)
    fun get(): Int
}

class JeopardyScoreImpl @Inject constructor(
    private val preferences: JeopardyPreferences
): JeopardyScore {

    companion object {
        private const val SCORE_PREF_KEY = "JEOPARDY_SCORE_PREF_KEY"
    }

    override fun get() = preferences.getInt(SCORE_PREF_KEY, 0)

    override fun add(value: Int) {
        preferences.putInt(
            SCORE_PREF_KEY,
            get() + value
        )
    }

    override fun subtract(value: Int) {
        add(0 - value)
    }

}
