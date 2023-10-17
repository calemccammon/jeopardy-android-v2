package com.cale.mccammon.jeopardy.feature.domain

import android.content.res.Resources
import android.text.Html
import com.cale.mccammon.jeopardy.feature.R
import com.cale.mccammon.jeopardy.feature.data.JeopardyInvalidQuestionException
import com.cale.mccammon.jeopardy.feature.data.model.Question
import com.cale.mccammon.jeopardy.feature.presentation.model.JeopardyAcknowledgment
import com.cale.mccammon.jeopardy.feature.presentation.model.JeopardyQuestion
import javax.inject.Inject

interface JeopardyModelMapper {
    fun mapQuestion(questions: List<Question>): JeopardyQuestion
    fun fromHtml(text: String): String
    fun buildSubmissionAcknowledgment(isCorrect: Boolean): JeopardyAcknowledgment
}

class JeopardyModelMapperImpl @Inject constructor(
    private val resources: Resources
): JeopardyModelMapper {

    @Throws(JeopardyInvalidQuestionException::class)
    override fun mapQuestion(questions: List<Question>): JeopardyQuestion {
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
                fromHtml(it.category!!.title!!),
                fromHtml(it.question!!),
                it.answer!!,
                it.value!!
            )
        }
    }

    override fun fromHtml(text: String): String {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            .toString()
            .replace("\n", "")
            .trim()
    }

    override fun buildSubmissionAcknowledgment(isCorrect: Boolean): JeopardyAcknowledgment {
        return JeopardyAcknowledgment(
            resources.getString(
                if (isCorrect) R.string.jeopardy_correct else R.string.jeopardy_try_again
            ),
            resources.getString(
                if (isCorrect) R.string.jeopardy_correct else R.string.jeopardy_try_again
            )
        )
    }
}