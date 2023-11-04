package com.cale.mccammon.jeopardy.feature.presentation.play.model

import com.google.gson.annotations.SerializedName

data class JeopardyQuestion(
    @SerializedName("id") val id: Int,
    @SerializedName("category") val category: String,
    @SerializedName("question") val question: String,
    @SerializedName("answer") val answer: String,
    @SerializedName("value") val value: Int
)
