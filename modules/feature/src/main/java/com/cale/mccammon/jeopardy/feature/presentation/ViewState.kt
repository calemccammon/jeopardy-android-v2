package com.cale.mccammon.jeopardy.feature.presentation

sealed class ViewState {

    data class Question(
        val category: String,
        val question: String,
        val answer: String,
        val value: Int
    )

    object Inactive : ViewState()

    object Loading : ViewState()

    data class ShowRandomQuestion(
        val question: Question
    ): ViewState()

    data class ShowAnswer(
        val answer: String
    ): ViewState()

    data class EvaluateSubmission(
        val answer: String,
        val question: Question
    ) : ViewState()

    object Error : ViewState()
}
