package com.cale.mccammon.jeopardy.feature.presentation.play.model

data class JeopardySubmission(
    val answer: String,
    val isCorrect: Boolean,
    val acknowledgment: JeopardyAcknowledgment
)
