package com.cale.mccammon.jeopardy.feature.presentation.model

data class JeopardyQuestion(
    val category: String,
    val question: String,
    val answer: String,
    val value: Int
)
