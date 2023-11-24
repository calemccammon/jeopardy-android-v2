package com.cale.mccammon.jeopardy.feature.presentation.play

import android.app.Application
import com.cale.mccammon.jeopardy.feature.MainDispatcherRule
import com.cale.mccammon.jeopardy.feature.data.JeopardyRepository
import com.cale.mccammon.jeopardy.feature.data.model.Category
import com.cale.mccammon.jeopardy.feature.data.model.Question
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.domain.JeopardyLogger
import com.cale.mccammon.jeopardy.feature.domain.JeopardyModelMapper
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayEvent
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayState
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyQuestion
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    fun handleDismissSubmission() = runTest {
        launch {
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
    }
}