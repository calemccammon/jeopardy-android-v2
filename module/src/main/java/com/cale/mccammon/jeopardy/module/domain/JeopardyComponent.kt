package com.cale.mccammon.jeopardy.module.domain

import com.cale.mccammon.jeopardy.module.data.JeopardyRepository
import com.cale.mccammon.jeopardy.module.data.Network

interface JeopardyComponent {
    val network: Network
    val repository: JeopardyRepository
}