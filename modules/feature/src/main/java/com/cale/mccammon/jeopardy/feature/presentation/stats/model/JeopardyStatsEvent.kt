package com.cale.mccammon.jeopardy.feature.presentation.stats.model

sealed class JeopardyStatsEvent {
    object ShowStats : JeopardyStatsEvent()
    object ClearStats : JeopardyStatsEvent()
    data class ExpandItem(val item: JeopardyHistoryItem) : JeopardyStatsEvent()
}