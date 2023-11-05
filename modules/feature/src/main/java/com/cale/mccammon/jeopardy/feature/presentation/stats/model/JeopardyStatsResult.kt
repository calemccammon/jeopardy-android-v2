package com.cale.mccammon.jeopardy.feature.presentation.stats.model

sealed class JeopardyStatsResult {
    data class ExpandedItem(val item: JeopardyHistoryItem?): JeopardyStatsResult()
    object StatsCleared : JeopardyStatsResult()
    object ShowingStats: JeopardyStatsResult()
}
