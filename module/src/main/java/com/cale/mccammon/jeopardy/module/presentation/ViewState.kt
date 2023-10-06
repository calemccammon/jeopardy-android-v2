package com.cale.mccammon.jeopardy.module.presentation

sealed class ViewState {
    object Inactive : ViewState()
    object Loading : ViewState()

    data class ShowRandomQuestion(
        val text: String
    ): ViewState()

    object Error : ViewState()
}
