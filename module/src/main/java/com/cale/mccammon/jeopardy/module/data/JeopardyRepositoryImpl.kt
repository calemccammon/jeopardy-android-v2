package com.cale.mccammon.jeopardy.module.data

import com.cale.mccammon.jeopardy.module.data.model.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class JeopardyRepositoryImpl @Inject constructor(
    private val network: Network
) : JeopardyRepository {
    override fun getRandomQuestion(): Flow<List<Question>> {
        return flow { emit(network.getRandomQuestion()) }
    }
}