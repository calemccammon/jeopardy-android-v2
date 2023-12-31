package com.cale.mccammon.jeopardy.android.dependency

import com.cale.mccammon.jeopardy.feature.data.JeopardyRepository
import com.cale.mccammon.jeopardy.feature.data.JeopardyNetwork
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.domain.JeopardyHistory
import com.cale.mccammon.jeopardy.feature.domain.JeopardyHtmlParser
import com.cale.mccammon.jeopardy.feature.domain.JeopardyLogger
import com.cale.mccammon.jeopardy.feature.domain.JeopardyModelMapper
import com.cale.mccammon.jeopardy.feature.domain.JeopardyPreferences
import com.cale.mccammon.jeopardy.feature.domain.JeopardyScore
import javax.inject.Inject

class JeopardyComponentImpl @Inject constructor(
    override val network: JeopardyNetwork,
    override val repository: JeopardyRepository,
    override val logger: JeopardyLogger,
    override val modelMapper: JeopardyModelMapper,
    override val preferences: JeopardyPreferences,
    override val score: JeopardyScore,
    override val history: JeopardyHistory,
    override val htmlParser: JeopardyHtmlParser
): JeopardyComponent