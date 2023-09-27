package com.cale.mccammon.jeopardy.network.data

import com.cale.mccammon.jeopardy.network.data.model.Question
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class NetworkImpl : Network {
    interface Service {
        @GET("random")
        suspend fun getRandomQuestion(): List<Question>
    }

    private val baseUrl: String = "http://jservice.io/api/"

    private val service: Service by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Service::class.java)
    }

    override suspend fun getRandomQuestion(): List<Question> {
        return service.getRandomQuestion()
    }
}