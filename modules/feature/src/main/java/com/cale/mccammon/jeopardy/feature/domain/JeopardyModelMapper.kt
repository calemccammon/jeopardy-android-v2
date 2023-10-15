package com.cale.mccammon.jeopardy.feature.domain

import android.text.Html
import com.cale.mccammon.jeopardy.feature.data.JeopardyInvalidQuestionException
import com.cale.mccammon.jeopardy.feature.data.model.Question
import com.cale.mccammon.jeopardy.feature.presentation.JeopardyQuestion

object JeopardyModelMapper {

    @Throws(JeopardyInvalidQuestionException::class)
    fun mapQuestion(questions: List<Question>): JeopardyQuestion {
        return questions.first().apply {
            // Data from the API isn't the cleanest
            // Check the validity up front
            val isInvalid = category?.title.isNullOrBlank()
                    || question.isNullOrBlank()
                    || answer.isNullOrBlank()
                    || value == null

            if (isInvalid) {
                throw JeopardyInvalidQuestionException(this)
            }
        }.let {
            JeopardyQuestion(
                it.category!!.title!!.fromHtml(),
                it.question!!.fromHtml(),
                it.answer!!,
                it.value!!
            )
        }
    }

    fun String.fromHtml(): String {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
            .toString()
            .replace("\n", "")
            .trim()
    }
}