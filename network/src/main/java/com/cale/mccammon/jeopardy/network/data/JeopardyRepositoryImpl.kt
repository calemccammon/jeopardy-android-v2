package com.cale.mccammon.jeopardy.network.data

import com.cale.mccammon.jeopardy.network.data.model.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class JeopardyRepositoryImpl(
    private val network: Network
) : JeopardyRepository {
    override fun getRandomQuestion(): Flow<List<Question>> {
        return flow { emit(network.getRandomQuestion()) }
    }
}