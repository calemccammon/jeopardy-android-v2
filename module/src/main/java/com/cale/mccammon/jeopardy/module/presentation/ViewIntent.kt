package com.cale.mccammon.jeopardy.module.presentation

sealed class ViewIntent {
    object GetRandomQuestion : ViewIntent()
}