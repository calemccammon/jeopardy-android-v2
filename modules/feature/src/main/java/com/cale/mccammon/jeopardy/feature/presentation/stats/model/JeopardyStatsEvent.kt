package com.cale.mccammon.jeopardy.feature.presentation.stats.model

sealed class JeopardyStatsEvent {
    object ShowStats : JeopardyStatsEvent()
}