package com.cale.mccammon.jeopardy.android.dependency

import com.cale.mccammon.jeopardy.module.data.JeopardyRepository
import com.cale.mccammon.jeopardy.module.data.JeopardyNetwork
import com.cale.mccammon.jeopardy.module.domain.JeopardyComponent
import javax.inject.Inject

class JeopardyComponentImpl @Inject constructor(
    override val network: JeopardyNetwork,
    override val repository: JeopardyRepository
): JeopardyComponent