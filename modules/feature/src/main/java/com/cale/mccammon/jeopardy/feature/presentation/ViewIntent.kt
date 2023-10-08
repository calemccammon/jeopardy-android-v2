package com.cale.mccammon.jeopardy.feature.presentation

import com.cale.mccammon.jeopardy.feature.data.model.Question

sealed class ViewIntent {
    object GetRandomQuestion : ViewIntent()
    data class SetRandomQuestion(val question: Question) : ViewIntent()
    data class SubmitAnswer(val answer: String, val question: Question) : ViewIntent()
    data class RevealAnswer(val question: Question) : ViewIntent()
}