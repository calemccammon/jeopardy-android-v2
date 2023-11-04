package com.cale.mccammon.jeopardy.feature.presentation.play

import android.text.Html
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.cale.mccammon.jeopardy.feature.R
import com.cale.mccammon.jeopardy.feature.presentation.JeopardyAlertDialog
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayEvent
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyPlayState
import com.cale.mccammon.jeopardy.feature.presentation.play.model.JeopardyQuestion
import com.cale.mccammon.jeopardy.theme.Padding

internal class JeopardyStateViewPreviewParameter : PreviewParameterProvider<JeopardyPlayState> {
    override val values: Sequence<JeopardyPlayState> = sequenceOf(
        JeopardyPlayState(
            false,
            JeopardyQuestion(
                0,
                "Category",
                "Question",
                "Answer",
                100
            ),
            false,
            null
        )
    )
}

@Composable
fun JeopardyView(
    viewModel: JeopardyPlayViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    JeopardyStateView(
        state = state
    ) { event ->
        viewModel.handleEvent(event)
    }
}

@Preview(showBackground = true)
@Composable
fun JeopardyStateView(
    @PreviewParameter(JeopardyStateViewPreviewParameter::class)
    state: JeopardyPlayState,
    handleEvent: (JeopardyPlayEvent) -> Unit = { }
) {
    val scrollState = ScrollState(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Padding.Large)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val revealAnswerDialog = remember { mutableStateOf(false) }

        val submitAnswerDialog = remember { mutableStateOf(false) }

        val submittedAnswer = remember { mutableStateOf("") }

        submitAnswerDialog.value = state.submission != null

        if (revealAnswerDialog.value) {
            JeopardyAlertDialog(
                onConfirmation = { 
                    revealAnswerDialog.value = false
                    submittedAnswer.value = ""
                    handleEvent.invoke(
                        JeopardyPlayEvent.GetRandomQuestion
                    )
                },
                dialogTitle = stringResource(id = R.string.jeopardy_reveal_answer_title),
                dialogText = Html.fromHtml(
                    stringResource(id = R.string.jeopardy_reveal_answer_body, state.question!!.answer),
                    Html.FROM_HTML_MODE_LEGACY
                ).toString()
            )
        }

        if (submitAnswerDialog.value) {
            JeopardyAlertDialog(
                onConfirmation = {
                    handleEvent.invoke(
                        JeopardyPlayEvent.DismissSubmission(
                            state.submission!!.isCorrect
                        )
                    )
                    submittedAnswer.value = ""
                },
                dialogTitle = state.submission!!.acknowledgment.title,
                dialogText = state.submission.acknowledgment.body
            )
        }

        JeopardyQuestionBox(state = state)

        Spacer(modifier = Modifier.height(Padding.XXLarge))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = submittedAnswer.value,
            onValueChange = {
                submittedAnswer.value = it
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    handleEvent.invoke(
                        JeopardyPlayEvent.SendAnswer(
                            submittedAnswer.value
                        )
                    )
                }
            )
        )

        Spacer(modifier = Modifier.height(Padding.Large))

        JeopardyButtonColumn(
            onSubmit = {
                handleEvent.invoke(JeopardyPlayEvent.SendAnswer(submittedAnswer.value))
            },
            onSkip = {
                handleEvent.invoke(JeopardyPlayEvent.GetRandomQuestion)
            }
        ) {
            revealAnswerDialog.value = true
            handleEvent.invoke(JeopardyPlayEvent.RevealAnswer)
        }
    }
}

@Composable
fun JeopardyQuestionBox(state: JeopardyPlayState) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .background(Color.Blue)
        .padding(Padding.Large),
    verticalArrangement = Arrangement.SpaceBetween
) {
    when {
        state.isLoading -> {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        else -> {
            JeopardyCategoryRow(category = state.question!!.category)
            JeopardyQuestionRow(question = state.question.question)
            Spacer(modifier = Modifier.height(Padding.XLarge))
            JeopardyValueRow(value = state.question.value.toString())
        }
    }
}


@Composable
fun JeopardyCategoryRow(category: String) = Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
) {
    Text(text = category, color = Color.White)
}

@Composable
fun JeopardyQuestionRow(question: String) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(
            vertical = Padding.Medium
        ),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.Top
) {
    Text(text = question, color = Color.White)
}

@Composable
fun JeopardyValueRow(value: String) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .background(Color.Blue),
    horizontalArrangement = Arrangement.End,
    verticalAlignment = Alignment.Bottom
) {
    Text(text = value, color = Color.White)
}

@Composable
fun JeopardyButtonColumn(
    onSubmit: () -> Unit,
    onSkip: () -> Unit,
    onReveal: () -> Unit
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = Padding.XLarge)
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSubmit,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White
        )
    ) {
        Text(text = stringResource(id = R.string.jeopardy_submit))
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSkip,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White
        )
    ) {
        Text(text = stringResource(id = R.string.jeopardy_skip))
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onReveal,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White
        )
    ) {
        Text(text = stringResource(id = R.string.jeopardy_reveal))
    }
}