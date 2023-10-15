package com.cale.mccammon.jeopardy.feature.data

import com.cale.mccammon.jeopardy.feature.data.model.Question

class JeopardyInvalidQuestionException(
    question: Question
) : RuntimeException(
    question.toString()
)