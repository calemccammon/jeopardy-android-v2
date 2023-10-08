package com.cale.mccammon.jeopardy.feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.domain.JeopardyModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JeopardyViewModel @Inject constructor(
    private val component: JeopardyComponent
) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Inactive)

    val viewState: StateFlow<ViewState> = _viewState

    init {
        handleIntent(ViewIntent.GetRandomQuestion)
    }

    private fun reduce(state: ViewState, intent: ViewIntent): ViewState {
        return when (intent) {
            is ViewIntent.GetRandomQuestion -> {
                ViewState.Loading
            }
            is ViewIntent.SetRandomQuestion -> {
                ViewState.ShowRandomQuestion(
                    intent.question
                )
            }
            is ViewIntent.RevealAnswer -> {
                ViewState.ShowAnswer(intent.question.answer)
            }
            is ViewIntent.SubmitAnswer -> {
                ViewState.EvaluateSubmission(
                    intent.answer,
                    intent.question
                )
            }
        }.also {
            component.logger.d("Previous State: $state")
            component.logger.d("New State: $it")
        }
    }

    fun handleIntent(intent: ViewIntent) {
        viewModelScope.launch {
            try {
                when (intent) {
                    is ViewIntent.GetRandomQuestion -> {
                        withContext(Dispatchers.IO) {
                            component.repository.getRandomQuestion()
                        }.collect { questions ->
                            val newState = reduce(
                                _viewState.value,
                                ViewIntent.SetRandomQuestion(
                                    JeopardyModelMapper.mapQuestion(
                                        questions
                                    )
                                )
                            )
                            _viewState.emit(newState)
                        }
                    }

                    else -> {
                        val newState = reduce(
                            _viewState.value,
                            intent
                        )
                        _viewState.emit(newState)
                    }
                }
            } catch (ex: Exception) {
                component.logger.e(ex)
                val newState = reduce(
                    ViewState.Error,
                    intent
                )
                _viewState.emit(newState)
            }
        }
    }

}