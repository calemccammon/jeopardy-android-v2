package com.cale.mccammon.jeopardy.feature.data

import okhttp3.Interceptor

data class JeopardyNetworkConfig(
    val url: String,
    val interceptors: List<Interceptor>
)
