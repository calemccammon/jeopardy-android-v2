package com.cale.mccammon.jeopardy.feature.presentation.play.model


data class JeopardyPlayState(
    val isLoading: Boolean = true,
    val question: JeopardyQuestion? = null,
    val revealAnswer: Boolean = false,
    val submission: JeopardySubmission? = null
)
