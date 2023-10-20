package com.cale.mccammon.jeopardy.feature.domain

import com.cale.mccammon.jeopardy.feature.data.JeopardyRepository
import com.cale.mccammon.jeopardy.feature.data.JeopardyNetwork

interface JeopardyComponent {
    val network: JeopardyNetwork
    val repository: JeopardyRepository
    val logger: JeopardyLogger
    val modelMapper: JeopardyModelMapper
    val preferences: JeopardyPreferences
    val score: JeopardyScore
}