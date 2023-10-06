package com.cale.mccammon.jeopardy.module.domain

import com.cale.mccammon.jeopardy.module.data.JeopardyRepository
import com.cale.mccammon.jeopardy.module.data.JeopardyNetwork

interface JeopardyComponent {
    val network: JeopardyNetwork
    val repository: JeopardyRepository
}