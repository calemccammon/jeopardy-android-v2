package com.cale.mccammon.jeopardy.feature.presentation.model

sealed class JeopardyPlayEvent {
    object GetRandomQuestion : JeopardyPlayEvent()

    object RevealAnswer : JeopardyPlayEvent()

    data class SendAnswer(
        val answer: String
    ) : JeopardyPlayEvent()

    data class ShowToast(
        val isCorrect: Boolean,
        val value: Int
    ): JeopardyPlayEvent()
}