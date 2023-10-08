package com.cale.mccammon.jeopardy.feature.presentation

import com.cale.mccammon.jeopardy.feature.data.model.Question

sealed class ViewState {
    object Inactive : ViewState()
    object Loading : ViewState()

    data class ShowRandomQuestion(
        val question: Question
    ): ViewState()

    object Error : ViewState()
}
