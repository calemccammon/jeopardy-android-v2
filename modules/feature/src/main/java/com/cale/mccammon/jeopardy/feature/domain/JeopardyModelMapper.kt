package com.cale.mccammon.jeopardy.feature.domain

import com.cale.mccammon.jeopardy.feature.data.model.Question
import com.cale.mccammon.jeopardy.feature.presentation.ViewState

object JeopardyModelMapper {
    fun mapQuestion(questions: List<Question>): ViewState.Question {
        return questions.first().let {
            ViewState.Question(
                it.category.title,
                it.question,
                it.answer,
                it.value
            )
        }
    }
}