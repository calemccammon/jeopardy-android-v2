package com.cale.mccammon.jeopardy.feature.presentation.model

sealed class JeopardyPlayResult {
    data class SetRandomQuestion(
        val question: JeopardyQuestion
    ): JeopardyPlayResult()

    data class AnswerEvaluated(
        val answer: String,
        val isCorrect: Boolean
    ): JeopardyPlayResult()
}