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
    fun buildSubmissionAcknowledgment(
        isCorrect: Boolean,
        value: Int,
        isInHistory: Boolean
    ): JeopardyAcknowledgment
}

class JeopardyModelMapperImpl @Inject constructor(
    private val resources: Resources,
    private val score: JeopardyScore,
    private val htmlParser: JeopardyHtmlParser
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
                htmlParser.fromHtml(it.category!!.title!!),
                htmlParser.fromHtml(it.question!!),
                it.answer!!,
                it.value!!
            )
        }
    }

    override fun buildSubmissionAcknowledgment(
        isCorrect: Boolean,
        value: Int,
        isInHistory: Boolean
    ): JeopardyAcknowledgment {
        return JeopardyAcknowledgment(
            resources.getString(
                if (isCorrect && !isInHistory) {
                    R.string.jeopardy_correct
                } else {
                    R.string.jeopardy_try_again
                }
            ),
            when {
                isCorrect && isInHistory -> {
                    resources.getString(
                        R.string.jeopardy_score_remains_correct,
                        score.get().toString()
                    )
                }
                !isCorrect && isInHistory -> {
                    resources.getString(
                        R.string.jeopardy_score_remains_incorrect,
                        score.get().toString()
                    )
                }
                else -> {
                    resources.getString(
                        if (isCorrect) {
                            R.string.jeopardy_increase
                        } else {
                            R.string.jeopardy_decrease
                        },
                        value.toString(),
                        score.get().toString()
                    )
                }
            }
        )
    }
}
