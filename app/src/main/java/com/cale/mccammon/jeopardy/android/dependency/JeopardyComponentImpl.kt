package com.cale.mccammon.jeopardy.android.dependency

import com.cale.mccammon.jeopardy.feature.data.JeopardyRepository
import com.cale.mccammon.jeopardy.feature.data.JeopardyNetwork
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.domain.JeopardyLogger
import com.cale.mccammon.jeopardy.feature.domain.JeopardyModelMapper
import javax.inject.Inject

class JeopardyComponentImpl @Inject constructor(
    override val network: JeopardyNetwork,
    override val repository: JeopardyRepository,
    override val logger: JeopardyLogger,
    override val modelMapper: JeopardyModelMapper
): JeopardyComponent