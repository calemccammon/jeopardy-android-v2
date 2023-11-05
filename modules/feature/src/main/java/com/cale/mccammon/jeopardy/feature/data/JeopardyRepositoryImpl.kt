package com.cale.mccammon.jeopardy.feature.data

import com.cale.mccammon.jeopardy.feature.data.model.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class JeopardyRepositoryImpl @Inject constructor(
    private val network: JeopardyNetwork
) : JeopardyRepository {
    override fun getRandomQuestion(): Flow<List<Question>> {
        return flow { emit(network.getRandomQuestion()) }
    }
}
