package com.cale.mccammon.jeopardy.feature.presentation

import androidx.lifecycle.viewModelScope
import com.cale.mccammon.jeopardy.feature.data.JeopardyInvalidQuestionException
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.domain.JeopardyModelMapper
import com.cale.mccammon.jeopardy.feature.domain.JeopardyModelMapper.fromHtml
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class JeopardyQuestion(
    val category: String,
    val question: String,
    val answer: String,
    val value: Int
)

data class JeopardySubmission(
    val answer: String,
    val isCorrect: Boolean
)

data class JeopardyPlayState(
    val isLoading: Boolean = true,
    val question: JeopardyQuestion? = null,
    val revealAnswer: Boolean = false,
    val submission: JeopardySubmission? = null
)

sealed class JeopardyPlayEvent {
    object GetRandomQuestion : JeopardyPlayEvent()

    object RevealAnswer : JeopardyPlayEvent()

    data class SendAnswer(
        val answer: String
    ) : JeopardyPlayEvent()

    data class ShowToast(
        val isCorrect: Boolean,
        val value: Int
    ): JeopardyPlayEvent()
}

sealed class JeopardyPlayResult {
    data class SetRandomQuestion(
        val question: JeopardyQuestion
    ): JeopardyPlayResult()

    data class AnswerEvaluated(
        val answer: String,
        val isCorrect: Boolean
    ): JeopardyPlayResult()
}

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
                    is JeopardyPlayEvent.GetRandomQuestion -> {
                        withContext(Dispatchers.IO) {
                            component.repository.getRandomQuestion()
                        }.retry(
                            1L
                        ) {
                            component.logger.e(it)
                            it is JeopardyInvalidQuestionException
                        }.collect { questions ->
                            _state.tryEmit(
                                handleResult(
                                    _state.value,
                                    JeopardyPlayResult.SetRandomQuestion(
                                        JeopardyModelMapper.mapQuestion(
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
            is JeopardyPlayResult.SetRandomQuestion -> {
                JeopardyPlayState(
                    isLoading = false,
                    result.question,
                    false,
                    null
                )
            }
            is JeopardyPlayResult.AnswerEvaluated -> {
                JeopardyPlayState(
                    isLoading = false,
                    state.question,
                    false,
                    JeopardySubmission(
                        result.answer,
                        result.isCorrect
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
        return fromHtml().replace(
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
        ).lowercase()
    }
}