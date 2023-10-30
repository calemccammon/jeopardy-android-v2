package com.cale.mccammon.jeopardy.feature.presentation.play.model

sealed class JeopardyPlayEvent {
    object GetRandomQuestion : JeopardyPlayEvent()

    data class DismissSubmission(
        val isCorrect: Boolean
    ): JeopardyPlayEvent()

    object RevealAnswer : JeopardyPlayEvent()

    data class SendAnswer(
        val answer: String
    ) : JeopardyPlayEvent()

    data class ShowToast(
        val isCorrect: Boolean,
        val value: Int
    ): JeopardyPlayEvent()
}