package com.cale.mccammon.jeopardy.feature.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.cale.mccammon.jeopardy.feature.presentation.theme.Padding
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
        modifier = Modifier.fillMaxSize()
            .padding(Padding.Large)
    ) {
        JeopardyQuestionBox(state = state)
    }
}

@Composable
fun JeopardyQuestionBox(state: ViewState) {
    Box {
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