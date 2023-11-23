package com.cale.mccammon.jeopardy.feature.domain

import android.content.res.Resources
import com.cale.mccammon.jeopardy.feature.R
import com.cale.mccammon.jeopardy.feature.data.JeopardyInvalidQuestionException
import com.cale.mccammon.jeopardy.feature.data.model.Category
import com.cale.mccammon.jeopardy.feature.data.model.Question
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyAcknowledgment
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyQuestion
import com.google.common.truth.Truth
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class JeopardyModelMapperImplTest {

    @Mock
    private lateinit var resources: Resources

    @Mock
    private lateinit var score: JeopardyScore

    @Mock
    private lateinit var htmlParser: JeopardyHtmlParser

    private lateinit var mapper: JeopardyModelMapper

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mapper = JeopardyModelMapperImpl(
            resources,
            score,
            htmlParser
        )
    }

    @Test
    @Suppress("LongMethod")
    fun mapQuestion() {
        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        1,
                        null,
                        "question",
                        1,
                        Category("title")
                    )
                )
            )
        }

        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        1,
                        "",
                        "question",
                        1,
                        Category("title")
                    )
                )
            )
        }

        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        0,
                        "answer",
                        "question",
                        1,
                        Category("title")
                    )
                )
            )
        }

        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        null,
                        "answer",
                        "question",
                        1,
                        Category("title")
                    )
                )
            )
        }

        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        1,
                        "answer",
                        null,
                        1,
                        Category("title")
                    )
                )
            )
        }

        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        1,
                        "answer",
                        "",
                        1,
                        Category("title")
                    )
                )
            )
        }

        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        1,
                        "answer",
                        "question",
                        0,
                        Category("title")
                    )
                )
            )
        }

        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        1,
                        "answer",
                        "question",
                        null,
                        Category("title")
                    )
                )
            )
        }

        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        1,
                        "answer",
                        "question",
                        1,
                        null
                    )
                )
            )
        }

        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        1,
                        "answer",
                        "question",
                        null,
                        Category(null)
                    )
                )
            )
        }

        Assert.assertThrows(
            JeopardyInvalidQuestionException::class.java,
        ) {
            mapper.mapQuestion(
                listOf(
                    Question(
                        1,
                        "answer",
                        "question",
                        null,
                        Category("")
                    )
                )
            )
        }

        whenever(htmlParser.fromHtml("title")).thenReturn("title")
        whenever(htmlParser.fromHtml("question")).thenReturn("question")

        Truth.assertThat(
            mapper.mapQuestion(
                listOf(
                    Question(
                        1,
                        "answer",
                        "question",
                        1,
                        Category("title")
                    )
                )
            )
        ).isEqualTo(
            JeopardyQuestion(
                1,
                "title",
                "question",
                "answer",
                1
            )
        )
    }

    @Test
    @Suppress("LongMethod")
    fun buildSubmissionAcknowledgment() {
        whenever(resources.getString(R.string.jeopardy_correct))
            .thenReturn("correct")
        whenever(resources.getString(R.string.jeopardy_try_again))
            .thenReturn("try again")
        whenever(resources.getString(R.string.jeopardy_score_remains_correct, "200"))
            .thenReturn("remains correct")
        whenever(resources.getString(R.string.jeopardy_score_remains_incorrect, "200"))
            .thenReturn("remains incorrect")
        whenever(resources.getString(R.string.jeopardy_increase, "100", "200"))
            .thenReturn("increase")
        whenever(resources.getString(R.string.jeopardy_decrease, "100", "200"))
            .thenReturn("decrease")
        whenever(score.get()).thenReturn(200)

        Truth.assertThat(
            mapper.buildSubmissionAcknowledgment(
                true,
                100,
                false
            )
        ).isEqualTo(
            JeopardyAcknowledgment(
                "correct",
                "increase"
            )
        )

        Truth.assertThat(
            mapper.buildSubmissionAcknowledgment(
                false,
                100,
                false
            )
        ).isEqualTo(
            JeopardyAcknowledgment(
                "try again",
                "decrease"
            )
        )

        Truth.assertThat(
            mapper.buildSubmissionAcknowledgment(
                true,
                100,
                true
            )
        ).isEqualTo(
            JeopardyAcknowledgment(
                "try again",
                "remains correct"
            )
        )

        Truth.assertThat(
            mapper.buildSubmissionAcknowledgment(
                false,
                100,
                true
            )
        ).isEqualTo(
            JeopardyAcknowledgment(
                "try again",
                "remains incorrect"
            )
        )
    }
}
