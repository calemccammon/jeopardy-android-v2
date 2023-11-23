package com.cale.mccammon.jeopardy.feature.domain

import android.content.res.Resources
import com.cale.mccammon.jeopardy.feature.data.JeopardyInvalidQuestionException
import com.cale.mccammon.jeopardy.feature.data.model.Category
import com.cale.mccammon.jeopardy.feature.data.model.Question
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
    fun buildSubmissionAcknowledgment() {

    }
}