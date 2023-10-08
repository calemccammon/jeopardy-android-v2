package com.cale.mccammon.jeopardy.feature.data.model


data class Question(
    val answer: String,
    val question: String,
    val value: Int,
    val category: Category
)
