package com.cale.mccammon.jeopardy.feature.presentation.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyStatsEvent
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyStatsState
import com.cale.mccammon.jeopardy.theme.Padding

@Composable
fun JeopardyStatsView(
    viewModel: JeopardyStatsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(
        key1 = state,
        block = {
            viewModel.handleEvent(JeopardyStatsEvent.ShowStats)
        }
    )

    JeopardyStatsStateView(
        state = state
    ) { event ->
        viewModel.handleEvent(event)
    }
}

@Composable
fun JeopardyStatsStateView(
    state: JeopardyStatsState,
    handleEvent: (JeopardyStatsEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(state.history) { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.question.question, maxLines = 1)
                Spacer(modifier = Modifier.size(Padding.Medium))
                Text(text = item.scoreEffect)
            }
        }
    }
}

@Composable
private fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}