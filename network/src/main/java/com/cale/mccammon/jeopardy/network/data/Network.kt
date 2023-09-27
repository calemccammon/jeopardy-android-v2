package com.cale.mccammon.jeopardy.network.data

import com.cale.mccammon.jeopardy.network.data.model.Question

interface Network {
    suspend fun getRandomQuestion(): List<Question>
}