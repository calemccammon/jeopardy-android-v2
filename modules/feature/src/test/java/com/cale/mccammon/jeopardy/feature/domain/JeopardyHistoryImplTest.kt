package com.cale.mccammon.jeopardy.feature.domain

import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyQuestion
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyHistoryItem
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class JeopardyHistoryImplTest {

    @Mock
    private lateinit var preferences: JeopardyPreferences

    private lateinit var history: JeopardyHistory

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        history = JeopardyHistoryImpl(preferences)
    }

    @Test
    fun add() {
        whenever(preferences.getString("JEOPARDY_HISTORY_PREF_KEY", "{\"items\":[]}"))
            .thenReturn("{\"items\":[]}")

        history.add(
            JeopardyHistoryItem(
                JeopardyQuestion(
                    1,
                    "category",
                    "question",
                    "answer",
                    1
                ),
                "effect"
            )
        )

        verify(preferences, times(1))
            .putString(
                "JEOPARDY_HISTORY_PREF_KEY",
                "{\"items\":[{\"question\":{\"id\":1,\"category\":\"category\",\"question\":" +
                        "\"question\",\"answer\":\"answer\",\"value\":1}," +
                        "\"scoreEffect\":\"effect\"}]}"
            )
    }
}
