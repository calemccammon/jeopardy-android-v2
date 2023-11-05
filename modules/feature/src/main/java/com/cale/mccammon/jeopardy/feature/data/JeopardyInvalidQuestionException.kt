package com.cale.mccammon.jeopardy.feature.data

import com.cale.mccammon.jeopardy.feature.data.model.Question

internal class JeopardyInvalidQuestionException(
    question: Question
) : RuntimeException(
    question.toString()
)
