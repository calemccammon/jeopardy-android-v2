package com.cale.mccammon.jeopardy.feature.presentation.stats.model

import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyQuestion
import com.google.gson.annotations.SerializedName

data class JeopardyHistoryItem(
    @SerializedName("question") val question: JeopardyQuestion,
    @SerializedName("scoreEffect") val scoreEffect: String
)
