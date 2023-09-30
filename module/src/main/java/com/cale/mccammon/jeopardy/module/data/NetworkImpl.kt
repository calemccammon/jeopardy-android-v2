package com.cale.mccammon.jeopardy.module.data

import com.cale.mccammon.jeopardy.module.data.model.Question
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Inject


class NetworkImpl @Inject constructor(): Network {
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