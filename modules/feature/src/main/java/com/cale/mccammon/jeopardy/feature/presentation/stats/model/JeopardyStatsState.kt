package com.cale.mccammon.jeopardy.feature.presentation.stats.model

data class JeopardyStatsState(
    val history: List<JeopardyHistoryItem>,
    val totalScore: Int
)
