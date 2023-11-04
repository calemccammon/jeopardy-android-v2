package com.cale.mccammon.jeopardy.feature.data.model


data class Question(
    val id: Int?,
    val answer: String?,
    val question: String?,
    val value: Int?,
    val category: Category?
)
