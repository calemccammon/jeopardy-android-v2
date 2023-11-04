package com.cale.mccammon.jeopardy.feature.presentation.stats

import androidx.lifecycle.viewModelScope
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.presentation.JeopardyViewModel
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyStatsEvent
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyStatsResult
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyStatsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JeopardyStatsViewModel @Inject constructor(
    private val component: JeopardyComponent
) : JeopardyViewModel<JeopardyStatsState, JeopardyStatsEvent, JeopardyStatsResult>() {

    override val initialState: JeopardyStatsState =
        JeopardyStatsState(
            component.history.get(),
            component.score.get()
        )

    private val _state = MutableStateFlow(initialState)

    override val state: StateFlow<JeopardyStatsState>
        get() = _state

    override fun handleResult(
        state: JeopardyStatsState,
        result: JeopardyStatsResult
    ): JeopardyStatsState {
        return state
    }

    override fun handleEvent(event: JeopardyStatsEvent) {
        viewModelScope.launch {
            when (event) {
                is JeopardyStatsEvent.ShowStats -> {
                    _state.tryEmit(
                        JeopardyStatsState(
                            component.history.get(),
                            component.score.get()
                        )
                    )
                }
            }
        }
    }
}