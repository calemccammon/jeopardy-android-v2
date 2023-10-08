package com.cale.mccammon.jeopardy.feature.presentation

import com.cale.mccammon.jeopardy.feature.data.model.Question

sealed class ViewIntent {
    object GetRandomQuestion : ViewIntent()
    data class SetRandomQuestion(val question: Question) : ViewIntent()
}