package com.cale.mccammon.jeopardy.feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JeopardyViewModel @Inject constructor(
    private val component: JeopardyComponent
) : ViewModel() {
    val viewState = MutableStateFlow<ViewState>(ViewState.Inactive)

    val viewIntent = Channel<ViewIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            viewIntent.consumeAsFlow().collect {
                when (it) {
                    is ViewIntent.GetRandomQuestion -> getRandomQuestion()
                }
            }
        }
    }

    private fun getRandomQuestion() {
        viewModelScope.launch {
            viewState.value = ViewState.Loading
            viewState.value = try {
                component.repository.getRandomQuestion().collect {

                }
                ViewState.ShowRandomQuestion("test")
            } catch (ex: Exception) {
                ViewState.Error
            }
        }
    }
}