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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

internal class JeopardyStateViewPreviewParameter : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState> = sequenceOf(
        ViewState.ShowRandomQuestion(
            ViewState.Question(
                "Category",
                "Question",
                "Answer",
                100
            )
        )
    )
}

@Composable
fun JeopardyView(
    viewModel: JeopardyViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsState()

    JeopardyStateView(
        state = state
    ) { intent ->
        viewModel.handleIntent(intent)
    }
}

@Preview(showBackground = true)
@Composable
fun JeopardyStateView(
    @PreviewParameter(JeopardyStateViewPreviewParameter::class)
    state: ViewState,
    handleIntent: (ViewIntent) -> Unit = { }
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(Padding.Large)
) {
    JeopardyQuestionBox(state = state)
    if (state is ViewState.ShowRandomQuestion) {
        JeopardyValueRow(value = state.question.value.toString())
    }
    JeopardyButtonColumn(state = state, handleIntent = handleIntent)
}

@Composable
fun JeopardyQuestionBox(state: ViewState) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.25f)
        .background(Color.Blue)
        .padding(Padding.Large)
) {
    when (state) {
        is ViewState.Inactive -> {
            CircularProgressIndicator()
        }

        is ViewState.Loading -> {
            CircularProgressIndicator()
        }

        is ViewState.Error -> {

        }

        is ViewState.ShowRandomQuestion -> {
            JeopardyCategoryRow(category = state.question.category)
            JeopardyQuestionRow(question = state.question.question)
        }

        else -> {}
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
        .background(Color.Blue)
        .padding(Padding.Large),
    horizontalArrangement = Arrangement.End,
    verticalAlignment = Alignment.Bottom
) {
    Text(text = value)
}

@Composable
fun JeopardyButtonColumn(
    state: ViewState,
    handleIntent: (ViewIntent) -> Unit
) = Column(
    modifier = Modifier.fillMaxWidth()
) {
    Button(
        onClick = {
        }
    ) {
        Text(text = stringResource(id = R.string.jeopardy_submit))
    }

    Button(
        onClick = {
            handleIntent.invoke(ViewIntent.GetRandomQuestion)
        }
    ) {
        Text(text = stringResource(id = R.string.jeopardy_skip))
    }

    Button(
        onClick = { }
    ) {
        Text(text = stringResource(id = R.string.jeopardy_reveal))
    }
}