package com.cale.mccammon.jeopardy.android.dependency

import com.cale.mccammon.jeopardy.module.data.JeopardyRepository
import com.cale.mccammon.jeopardy.module.data.Network
import com.cale.mccammon.jeopardy.module.domain.JeopardyComponent
import javax.inject.Inject

class JeopardyComponentImpl @Inject constructor(
    override val network: Network,
    override val repository: JeopardyRepository
): JeopardyComponent