package com.cale.mccammon.jeopardy.feature.domain

interface JeopardyLogger {
    fun d(message: String)
    fun e(ex: Throwable)

}