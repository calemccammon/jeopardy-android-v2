package com.cale.mccammon.jeopardy.feature.domain

import com.cale.mccammon.jeopardy.feature.data.model.Question
import com.cale.mccammon.jeopardy.feature.presentation.JeopardyQuestion

object JeopardyModelMapper {
    fun mapQuestion(questions: List<Question>): JeopardyQuestion {
        return questions.first().let {
            JeopardyQuestion(
                it.category.title,
                it.question,
                it.answer,
                it.value
            )
        }
    }
}