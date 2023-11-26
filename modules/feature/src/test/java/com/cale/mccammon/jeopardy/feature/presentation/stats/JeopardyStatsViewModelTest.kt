package com.cale.mccammon.jeopardy.feature.presentation.stats

import androidx.lifecycle.viewModelScope
import com.cale.mccammon.jeopardy.feature.MainDispatcherRule
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.domain.JeopardyHistory
import com.cale.mccammon.jeopardy.feature.domain.JeopardyPreferences
import com.cale.mccammon.jeopardy.feature.domain.JeopardyScore
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyQuestion
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyHistoryItem
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyStatsEvent
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyStatsState
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class JeopardyStatsViewModelTest {

    @get:Rule
    val rule: MainDispatcherRule = MainDispatcherRule()

    private val component: JeopardyComponent = mockk()

    private val score: JeopardyScore = mockk()

    private val history: JeopardyHistory = mockk()

    private val preferences: JeopardyPreferences = mockk()

    private lateinit var viewModel: JeopardyStatsViewModel

    private val scoreCount = 100

    private val item = JeopardyHistoryItem(
        JeopardyQuestion(
            1,
            "category",
            "question",
            "answer",
            scoreCount
        ),
        "effect"
    )

    private val items = listOf(item)

    @Before
    fun setUp() {
        every { component.score }.returns(score)
        every { component.history }.returns(history)
        every { score.get() }.returns(scoreCount)
        every { history.get() }.returns(items)
        every { component.preferences }.returns(preferences)
        every { preferences.clear() }.answers {  }
        viewModel = JeopardyStatsViewModel(component)
    }

    @Test
    fun testInitial() = runTest {
        Truth.assertThat(
            viewModel.initialState
        ).isEqualTo(
            JeopardyStatsState(
                items,
                scoreCount,
                null
            )
        )
    }

    @Test
    fun testClear() = runTest {
        viewModel.viewModelScope.launch {
            viewModel.handleEvent(
                JeopardyStatsEvent.ClearStats
            )
        }

        verify {
            preferences.clear()
        }

        Truth.assertThat(
            viewModel.state.value
        ).isEqualTo(
            JeopardyStatsState(
                emptyList(),
                0
            )
        )
    }

    @Test
    fun testShowStats() = runTest {
        viewModel.viewModelScope.launch {
            viewModel.handleEvent(
                JeopardyStatsEvent.ShowStats
            )
        }

        Truth.assertThat(
            viewModel.state.value
        ).isEqualTo(
            JeopardyStatsState(
                items,
                scoreCount
            )
        )
    }

    @Test
    fun testExpandItem() = runTest {
        viewModel.viewModelScope.launch {
            viewModel.handleEvent(
                JeopardyStatsEvent.ExpandItem(item)
            )
        }

        Truth.assertThat(
            viewModel.state.value
        ).isEqualTo(
            JeopardyStatsState(
                items,
                scoreCount,
                item
            )
        )

        viewModel.viewModelScope.launch {
            viewModel.handleEvent(
                JeopardyStatsEvent.ExpandItem(item)
            )
        }

        Truth.assertThat(
            viewModel.state.value
        ).isEqualTo(
            JeopardyStatsState(
                items,
                scoreCount,
                null
            )
        )
    }
}
