package com.cale.mccammon.jeopardy.feature.data

import com.cale.mccammon.jeopardy.feature.data.model.Question
import kotlinx.coroutines.flow.Flow

interface JeopardyRepository {
    fun getRandomQuestion(): Flow<List<Question>>
}
