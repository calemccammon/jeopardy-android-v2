package com.cale.mccammon.jeopardy.feature.domain

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class JeopardyScoreImplTest {

    @Mock
    private lateinit var preferences: JeopardyPreferences

    private lateinit var score: JeopardyScore

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        score = JeopardyScoreImpl(preferences)
    }

    @Test
    fun add() {
        whenever(preferences.getInt("JEOPARDY_SCORE_PREF_KEY", 0))
            .thenReturn(100)
        score.add(100)
        verify(preferences, times(1))
            .putInt("JEOPARDY_SCORE_PREF_KEY", 200)
    }

    @Test
    fun subtract() {
        whenever(preferences.getInt("JEOPARDY_SCORE_PREF_KEY", 0))
            .thenReturn(100)
        score.subtract(100)
        verify(preferences, times(1))
            .putInt("JEOPARDY_SCORE_PREF_KEY", 0)
    }
}
