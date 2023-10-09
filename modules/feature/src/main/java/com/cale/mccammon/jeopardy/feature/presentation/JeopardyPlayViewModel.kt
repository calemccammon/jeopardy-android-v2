package com.cale.mccammon.jeopardy.feature.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.domain.JeopardyModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class JeopardyQuestion(
    val category: String,
    val question: String,
    val answer: String,
    val value: Int
)

data class JeopardyPlayState(
    val isLoading: Boolean = true,
    val question: JeopardyQuestion? = null,
    val revealAnswer: Boolean = false,
    val submittedAnswer: String? = null
)

sealed class JeopardyPlayEvent {
    object GetRandomQuestion : JeopardyPlayEvent()
    data class SetRandomQuestion(
        val question: JeopardyQuestion
    ): JeopardyPlayEvent()

    object RevealAnswer : JeopardyPlayEvent()

    data class SendAnswer(
        val answer: String
    ) : JeopardyPlayEvent()
}

@HiltViewModel
class JeopardyPlayViewModel @Inject constructor(
    private val component: JeopardyComponent
) : JeopardyViewModel<JeopardyPlayState, JeopardyPlayEvent>() {

    override val initialState: JeopardyPlayState = JeopardyPlayState()

    private val _state: MutableStateFlow<JeopardyPlayState> = MutableStateFlow(initialState)
    override val state: StateFlow<JeopardyPlayState>
        get() = _state

    init {
        handleEvent(JeopardyPlayEvent.GetRandomQuestion)
    }

    override fun handleEvent(event: JeopardyPlayEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is JeopardyPlayEvent.GetRandomQuestion -> {
                        withContext(Dispatchers.IO) {
                            component.repository.getRandomQuestion()
                        }.collect { questions ->
                            _state.tryEmit(
                                reduce(
                                    _state.value,
                                    JeopardyPlayEvent.SetRandomQuestion(
                                        JeopardyModelMapper.mapQuestion(
                                            questions
                                        )
                                    )
                                )
                            )
                        }
                    }
                    else -> {
                    }
                }
            } catch (ex: Exception) {
                component.logger.e(ex)
            }
        }
    }

    override fun reduce(state: JeopardyPlayState, event: JeopardyPlayEvent): JeopardyPlayState {
        return when (event) {
            is JeopardyPlayEvent.GetRandomQuestion -> {
                state.copy(isLoading = true)
            }
            is JeopardyPlayEvent.SetRandomQuestion -> {
                state.copy(isLoading = false, question = event.question)
            }
            is JeopardyPlayEvent.RevealAnswer -> {
                state.copy(revealAnswer = true)
            }
            else -> {
                state
            }
        }.also { newState ->
            with(component.logger) {
                d("Event: $event")
                d("Previous State: $state")
                d("New State: $newState")
            }
        }
    }
}