package com.cale.mccammon.jeopardy.feature.domain

import android.content.res.Resources
import android.text.Html
import com.cale.mccammon.jeopardy.feature.R
import com.cale.mccammon.jeopardy.feature.data.JeopardyInvalidQuestionException
import com.cale.mccammon.jeopardy.feature.data.model.Question
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyAcknowledgment
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyQuestion
import javax.inject.Inject

interface JeopardyModelMapper {
    fun mapQuestion(questions: List<Question>): JeopardyQuestion
    fun fromHtml(text: String): String
    fun buildSubmissionAcknowledgment(
        isCorrect: Boolean,
        value: Int
    ): JeopardyAcknowledgment
}

class JeopardyModelMapperImpl @Inject constructor(
    private val resources: Resources,
    private val score: JeopardyScore
): JeopardyModelMapper {

    @Throws(JeopardyInvalidQuestionException::class)
    override fun mapQuestion(questions: List<Question>): JeopardyQuestion {
        return questions.first().apply {
            // Data from the API isn't the cleanest
            // Check the validity up front
            val isInvalid = category?.title.isNullOrBlank()
                    || question.isNullOrBlank()
                    || answer.isNullOrBlank()
                    || id == null || id == 0
                    || value == null || value == 0

            if (isInvalid) {
                throw JeopardyInvalidQuestionException(this)
            }
        }.let {
            JeopardyQuestion(
                it.id!!,
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

    override fun buildSubmissionAcknowledgment(
        isCorrect: Boolean,
        value: Int
    ): JeopardyAcknowledgment {
        return JeopardyAcknowledgment(
            resources.getString(
                if (isCorrect) R.string.jeopardy_correct else R.string.jeopardy_try_again
            ),
            resources.getString(
                if (isCorrect) R.string.jeopardy_increase else R.string.jeopardy_decrease,
                value.toString(),
                score.get().toString()
            )
        )
    }
}