package com.cale.mccammon.jeopardy.module.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

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
    viewModel.viewState.collectAsState().value
    Greeting(name = "Android")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}