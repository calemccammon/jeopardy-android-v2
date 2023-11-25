package com.cale.mccammon.jeopardy.feature.presentation.play

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.cale.mccammon.jeopardy.feature.R
import com.cale.mccammon.jeopardy.feature.MainDispatcherRule
import com.cale.mccammon.jeopardy.feature.data.JeopardyRepository
import com.cale.mccammon.jeopardy.feature.data.model.Category
import com.cale.mccammon.jeopardy.feature.data.model.Question
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.domain.JeopardyHistory
import com.cale.mccammon.jeopardy.feature.domain.JeopardyHtmlParser
import com.cale.mccammon.jeopardy.feature.domain.JeopardyLogger
import com.cale.mccammon.jeopardy.feature.domain.JeopardyModelMapper
import com.cale.mccammon.jeopardy.feature.domain.JeopardyScore
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyAcknowledgment
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayEvent
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayState
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyQuestion
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardySubmission
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyHistoryItem
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class JeopardyPlayViewModelTest {

    @get:Rule
    val rule: MainDispatcherRule = MainDispatcherRule()

    private val component: JeopardyComponent = mockk()

    private val application: Application = mockk()

    private val logger: JeopardyLogger = mockk()

    private val repository: JeopardyRepository = mockk()

    private val modelMapper: JeopardyModelMapper = mockk()

    private val history: JeopardyHistory = mockk()

    private val htmlParser: JeopardyHtmlParser = mockk()

    private val score: JeopardyScore = mockk()

    private val question = JeopardyQuestion(
        1,
        "title",
        "question",
        "answer",
        100
    )

    private lateinit var viewModel: JeopardyPlayViewModel

    @Before
    fun setUp() {
        every { component.repository }.returns(repository)

        every { component.logger }.returns(logger)

        every { component.modelMapper }.returns(modelMapper)

        every { modelMapper.mapQuestion(any()) }
            .returns(question)

        every { logger.d(any()) }.answers {  }

        every { component.history }.returns(history)

        every { component.htmlParser }.returns(htmlParser)

        every { component.score }.returns(score)

        every { score.subtract(any()) }.answers {  }

        every { score.add(any()) }.answers {  }

        every { application.getString(R.string.jeopardy_skipped) }
            .returns("skipped")

        coEvery { repository.getRandomQuestion() }.returns(
            MutableStateFlow(
                listOf(
                    Question(
                        1,
                        "answer",
                        "question",
                        100,
                        Category("title")
                    )
                )
            )
        )

        viewModel = JeopardyPlayViewModel(
            component,
            application
        )
    }

    @Test
    fun testInitial() = runTest {
        Truth.assertThat(
            viewModel.initialState
        ).isEqualTo(
            JeopardyPlayState()
        )
    }

    @Test
    fun handleDismissSubmission() = runTest {
        viewModel.viewModelScope.launch {
            viewModel.handleEvent(
                JeopardyPlayEvent.DismissSubmission(true)
            )
        }

        Truth.assertThat(
            viewModel.state.value
        ).isEqualTo(
            JeopardyPlayState(
                false,
                question,
                false,
                null
            )
        )

        viewModel.viewModelScope.launch {
            viewModel.handleEvent(
                JeopardyPlayEvent.DismissSubmission(false)
            )
        }

        Truth.assertThat(
            viewModel.state.value
        ).isEqualTo(
            JeopardyPlayState(
                false,
                question,
                false,
                null
            )
        )
    }

    @Test
    fun testSkipQuestion() = runTest {
        every { history.get() }.returns(
            listOf(
                JeopardyHistoryItem(
                    question,
                    ""
                )
            )
        )

        viewModel.viewModelScope.launch {
            viewModel.handleEvent(
                JeopardyPlayEvent.SkipQuestion
            )
        }

        Truth.assertThat(
            viewModel.state.value
        ).isEqualTo(
            JeopardyPlayState(
                false,
                question,
                false,
                null
            )
        )
    }

    @Test
    fun testSendAnswer() {
        val acknowledgment = JeopardyAcknowledgment(
            "title",
            "body"
        )

        val answer = "the \"\'Answer"

        val submission = JeopardySubmission(
            answer,
            true,
            acknowledgment
        )

        every { history.get() }.returns(listOf())

        every { history.add(any()) }.answers {  }

        every {
            modelMapper.buildSubmissionAcknowledgment(
                any(),
                any(),
                any()
            )
        }.returns(acknowledgment)

        every {
            htmlParser.fromHtml(any())
        }.returns(answer)

        viewModel.viewModelScope.launch {
            viewModel.handleEvent(
                JeopardyPlayEvent.SendAnswer(
                    answer
                )
            )
        }

        Truth.assertThat(
            viewModel.state.value
        ).isEqualTo(
            JeopardyPlayState(
                false,
                question,
                false,
                submission
            )
        )
    }
}
