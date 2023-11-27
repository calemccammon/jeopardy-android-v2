package com.cale.mccammon.jeopardy.feature

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.cale.mccammon.jeopardy.feature.presentation.play.compose.JeopardyStateView
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayState
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyQuestion
import com.cale.mccammon.jeopardy.theme.JeopardyAndroidTheme
import org.junit.Rule
import org.junit.Test

class JeopardyPlayTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val question = JeopardyQuestion(
        1,
        "category",
        "question",
        "answer",
        100
    )

    @Test
    fun testPlayScreen() {
        composeTestRule.setContent {
            JeopardyAndroidTheme {
                JeopardyStateView(
                    JeopardyPlayState(
                        false,
                        question,
                        false,
                        null
                    )
                ) {

                }
            }
        }

        composeTestRule.onNodeWithText(question.category)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(question.question)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(question.value.toString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Submit")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Skip")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Reveal")
            .assertIsDisplayed()
    }
}