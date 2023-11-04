package com.cale.mccammon.jeopardy.feature.presentation.play

import androidx.lifecycle.viewModelScope
import com.cale.mccammon.jeopardy.feature.data.JeopardyInvalidQuestionException
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.presentation.JeopardyViewModel
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayEvent
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayResult
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayState
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardySubmission
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyHistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JeopardyPlayViewModel @Inject constructor(
    private val component: JeopardyComponent
) : JeopardyViewModel<JeopardyPlayState, JeopardyPlayEvent, JeopardyPlayResult>() {

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
                    is JeopardyPlayEvent.DismissSubmission -> {
                        if (event.isCorrect) {
                            handleEvent(JeopardyPlayEvent.GetRandomQuestion)
                        } else {
                            _state.tryEmit(
                                handleResult(
                                    _state.value,
                                    JeopardyPlayResult.SubmissionDismissed
                                )
                            )
                        }
                    }
                    is JeopardyPlayEvent.GetRandomQuestion -> {
                        withContext(Dispatchers.IO) {
                            component.repository.getRandomQuestion()
                        }.catch {
                            component.logger.e(it)
                            handleEvent(event)
                        }.collect { questions ->
                            _state.tryEmit(
                                handleResult(
                                    _state.value,
                                    JeopardyPlayResult.SetRandomQuestion(
                                        component.modelMapper.mapQuestion(
                                            questions
                                        )
                                    )
                                )
                            )
                        }
                    }
                    is JeopardyPlayEvent.SendAnswer -> {
                        val isCorrect = event.answer.sanitize() == _state.value.question?.answer?.sanitize()
                        _state.tryEmit(
                            handleResult(
                                _state.value,
                                JeopardyPlayResult.AnswerEvaluated(
                                    event.answer,
                                    isCorrect
                                )
                            )
                        )
                    }
                    else -> {

                    }
                }
            } catch (ex: Exception) {
                component.logger.e(ex)
            }
        }
    }

    override fun handleResult(state: JeopardyPlayState, result: JeopardyPlayResult): JeopardyPlayState {
        return when (result) {
            is JeopardyPlayResult.SubmissionDismissed -> {
                state.copy(submission = null)
            }
            is JeopardyPlayResult.SetRandomQuestion -> {
                JeopardyPlayState(
                    isLoading = false,
                    result.question,
                    false,
                    null
                )
            }
            is JeopardyPlayResult.AnswerEvaluated -> {
                val isInHistory = component.history.get()
                    .find {
                        it.question.id == state.question!!.id
                    } != null

                if (!isInHistory && result.isCorrect) {
                    component.score.add(state.question!!.value)
                    component.history.add(
                        JeopardyHistoryItem(
                            state.question,
                            "+${state.question.value}"
                        )
                    )
                } else if (!isInHistory) {
                    component.score.subtract(state.question!!.value)
                    component.history.add(
                        JeopardyHistoryItem(
                            state.question,
                            "-${state.question.value}"
                        )
                    )
                }

                JeopardyPlayState(
                    isLoading = false,
                    state.question,
                    false,
                    JeopardySubmission(
                        result.answer,
                        result.isCorrect,
                        component.modelMapper.buildSubmissionAcknowledgment(
                            result.isCorrect,
                            state.question!!.value
                        )
                    )
                )
            }
        }.also { newState ->
            with(component.logger) {
                d("Event: $result")
                d("Previous State: $state")
                d("New State: $newState")
            }
        }
    }

    /**
     * The API we use is not the greatest in terms of formatting
     * the answer. For this reason, we need to sanitize the strings.
     * We also want to get rid of leading articles.
     */
    private fun String.sanitize(): String {
        return component.modelMapper.fromHtml(this).replace(
            "the ",
            "",
            true
        ).replace(
            "a ",
            "",
            true
        ).replace(
            "an ",
            "",
            true
        ).replace(
            "\"",
            ""
        ).replace(
            "\'",
            ""
        ).lowercase()
    }
}