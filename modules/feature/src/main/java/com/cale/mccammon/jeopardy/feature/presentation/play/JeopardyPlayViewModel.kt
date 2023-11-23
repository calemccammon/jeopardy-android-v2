package com.cale.mccammon.jeopardy.feature.presentation.play

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.cale.mccammon.jeopardy.feature.R
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.presentation.JeopardyViewModel
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayEvent
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayResult
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayState
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyQuestion
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardySubmission
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyHistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JeopardyPlayViewModel @Inject constructor(
    private val component: JeopardyComponent,
    private val application: Application
) : JeopardyViewModel<JeopardyPlayState, JeopardyPlayEvent, JeopardyPlayResult>() {

    override val initialState: JeopardyPlayState = JeopardyPlayState()

    private val _state: MutableStateFlow<JeopardyPlayState> = MutableStateFlow(initialState)
    override val state: StateFlow<JeopardyPlayState>
        get() = _state

    init {
        handleEvent(JeopardyPlayEvent.GetRandomQuestion)
    }

    @Suppress("TooGenericExceptionCaught")
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
                    is JeopardyPlayEvent.SkipQuestion -> {
                        addToHistory(_state.value.question, null)
                        handleEvent(JeopardyPlayEvent.GetRandomQuestion)
                    }
                    is JeopardyPlayEvent.GetRandomQuestion -> {
                        fetchRandomQuestion()
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
                    is JeopardyPlayEvent.RevealAnswer -> {
                        if (_state.value.submission == null) {
                            addToHistory(_state.value.question, null)
                        }
                    }
                }
            } catch (ex: Exception) {
                component.logger.e(ex)
            }
        }
    }

    private suspend fun fetchRandomQuestion() {
        withContext(Dispatchers.IO) {
            component.repository.getRandomQuestion()
        }.retry {
            component.logger.e(it)
            true
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
                if (result.answer.isBlank()) {
                    return state
                }

                val isInHistory = component.history.get()
                    .find {
                        it.question.id == state.question!!.id
                    } != null

                if (!isInHistory && result.isCorrect) {
                    component.score.add(state.question!!.value)
                    addToHistory(state.question, true)
                } else if (!isInHistory) {
                    component.score.subtract(state.question!!.value)
                    addToHistory(state.question, false)
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
                            state.question!!.value,
                            isInHistory
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
        return component.htmlParser.fromHtml(this).replace(
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

    private fun addToHistory(question: JeopardyQuestion?, isCorrect: Boolean?) {
        if (question == null) {
            return
        }

        val contains = component.history.get().find { it.question.id == question.id } != null

        if (contains) {
            return
        }

        JeopardyHistoryItem(
            question,
            when (isCorrect) {
                true -> {
                    "+${question.value}"
                }
                false -> {
                    "-${question.value}"
                }
                else -> {
                   application.getString(R.string.jeopardy_skipped)
                }
            }
        ).let {
            component.history.add(it)
        }
    }
}
