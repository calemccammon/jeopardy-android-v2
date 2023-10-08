package com.cale.mccammon.jeopardy.feature.presentation

sealed class ViewIntent {
    object GetRandomQuestion : ViewIntent()
    data class SetRandomQuestion(
        val question: ViewState.Question
    ) : ViewIntent()
    data class SubmitAnswer(
        val answer: String,
        val question: ViewState.Question
    ) : ViewIntent()
    data class RevealAnswer(
        val question: ViewState.Question
    ) : ViewIntent()
}