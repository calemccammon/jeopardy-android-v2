package com.cale.mccammon.jeopardy.module.data

import com.cale.mccammon.jeopardy.module.data.model.Question
import kotlinx.coroutines.flow.Flow

interface JeopardyRepository {
    fun getRandomQuestion(): Flow<List<Question>>
}