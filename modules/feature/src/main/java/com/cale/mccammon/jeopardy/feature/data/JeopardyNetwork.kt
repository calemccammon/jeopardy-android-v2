package com.cale.mccammon.jeopardy.feature.data

import com.cale.mccammon.jeopardy.feature.data.model.Question

interface JeopardyNetwork {
    suspend fun getRandomQuestion(): List<Question>
}