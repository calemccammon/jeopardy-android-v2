package com.cale.mccammon.jeopardy.feature.presentation

sealed class ViewIntent {
    object GetRandomQuestion : ViewIntent()
}