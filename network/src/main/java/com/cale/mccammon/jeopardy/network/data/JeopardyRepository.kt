package com.cale.mccammon.jeopardy.network.data

import com.cale.mccammon.jeopardy.network.data.model.Question
import kotlinx.coroutines.flow.Flow

interface JeopardyRepository {
    fun getRandomQuestion(): Flow<List<Question>>
}