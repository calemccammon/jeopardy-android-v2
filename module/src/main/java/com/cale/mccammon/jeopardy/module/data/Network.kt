package com.cale.mccammon.jeopardy.module.data

import com.cale.mccammon.jeopardy.module.data.model.Question

interface Network {
    suspend fun getRandomQuestion(): List<Question>
}