package com.cale.mccammon.jeopardy.feature.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

internal class JeopardyStateViewPreviewParameter : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState> = sequenceOf(
        ViewState.Inactive
    )
}

@Composable
fun JeopardyView(
    viewModel: JeopardyViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(key1 = Unit) {
        scope.launch {
            viewModel.viewIntent.send(ViewIntent.GetRandomQuestion)
        }
    }
    
    viewModel.viewState.collectAsState().value.let { state -> 
        JeopardyStateView(state = state)
    }
}

@Preview(showBackground = true)
@Composable
fun JeopardyStateView(
    @PreviewParameter(JeopardyStateViewPreviewParameter::class)
    state: ViewState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Padding.Large)
    ) {
        JeopardyQuestionBox(state = state)
        JeopardyButtonColumn(state = state)
    }
}

@Composable
fun JeopardyQuestionBox(state: ViewState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
            .background(Color.Blue)
            .padding(Padding.Large),
        contentAlignment = Alignment.Center
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

            }
        }
    }
}

@Composable
fun JeopardyButtonColumn(state: ViewState) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = {  }) {
            Text(text = stringResource(id = R.string.jeopardy_submit))
        }
    }
}