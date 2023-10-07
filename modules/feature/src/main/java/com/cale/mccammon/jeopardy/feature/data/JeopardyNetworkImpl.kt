package com.cale.mccammon.jeopardy.feature.data

import com.cale.mccammon.jeopardy.feature.data.model.Question
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Inject


class JeopardyNetworkImpl @Inject constructor(
    private val config: JeopardyNetworkConfig
): JeopardyNetwork {
    interface Service {
        @GET("random")
        suspend fun getRandomQuestion(): List<Question>
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder().apply {
            config.interceptors.forEach {
                addInterceptor(it)
            }
        }.build()
    }

    private val service: Service by lazy {
        Retrofit.Builder()
            .baseUrl(config.url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(Service::class.java)
    }

    override suspend fun getRandomQuestion(): List<Question> {
        return service.getRandomQuestion()
    }
}