package com.cale.mccammon.jeopardy.feature.presentation.model

data class JeopardySubmission(
    val answer: String,
    val isCorrect: Boolean,
    val acknowledgment: JeopardyAcknowledgment
)
