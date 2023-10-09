package com.cale.mccammon.jeopardy.feature.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.cale.mccammon.jeopardy.feature.R
import com.cale.mccammon.jeopardy.theme.Padding

internal class JeopardyStateViewPreviewParameter : PreviewParameterProvider<JeopardyPlayState> {
    override val values: Sequence<JeopardyPlayState> = sequenceOf(
        JeopardyPlayState(
            false,
            JeopardyQuestion(
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
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(Padding.Large),
    verticalArrangement = Arrangement.SpaceBetween
) {
    val openDialog = remember { mutableStateOf(state.revealAnswer) }

    if (openDialog.value) {
        JeopardyAlertDialog(
            onDismissRequest = { openDialog.value = false },
            onConfirmation = { openDialog.value = false },
            dialogTitle = "test",
            dialogText = "test"
        )
    }

    JeopardyQuestionBox(state = state)

    JeopardyButtonColumn(
        state = state,
        onSubmit = {
            handleEvent.invoke(JeopardyPlayEvent.SendAnswer("test"))
        },
        onSkip = {
            handleEvent.invoke(JeopardyPlayEvent.GetRandomQuestion)
        },
        onReveal = {
            openDialog.value = true
            handleEvent.invoke(JeopardyPlayEvent.RevealAnswer)
        }
    )
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
            CircularProgressIndicator()
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
    Text(text = category)
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
    Text(text = question)
}

@Composable
fun JeopardyValueRow(value: String) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .background(Color.Blue),
    horizontalArrangement = Arrangement.End,
    verticalAlignment = Alignment.Bottom
) {
    Text(text = value)
}

@Composable
fun JeopardyButtonColumn(
    state: JeopardyPlayState,
    onSubmit: () -> Unit,
    onSkip: () -> Unit,
    onReveal: () -> Unit
) = Column(
    modifier = Modifier.fillMaxWidth()
) {
    Button(
        onClick = onSubmit
    ) {
        Text(text = stringResource(id = R.string.jeopardy_submit))
    }

    Button(
        onClick = onSkip
    ) {
        Text(text = stringResource(id = R.string.jeopardy_skip))
    }

    Button(
        onClick = onReveal
    ) {
        Text(text = stringResource(id = R.string.jeopardy_reveal))
    }
}