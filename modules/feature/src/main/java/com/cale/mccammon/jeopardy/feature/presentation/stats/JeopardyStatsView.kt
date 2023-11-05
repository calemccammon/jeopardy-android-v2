package com.cale.mccammon.jeopardy.feature.presentation.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.cale.mccammon.jeopardy.feature.R
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyStatsEvent
import com.cale.mccammon.jeopardy.feature.presentation.stats.model.JeopardyStatsState
import com.cale.mccammon.jeopardy.theme.Padding

@Composable
fun JeopardyStatsView(
    viewModel: JeopardyStatsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    OnLifecycleEvent(onEvent = { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.handleEvent(JeopardyStatsEvent.ShowStats)
            }
            else -> {}
        }
    })

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
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = Padding.Small,
            vertical = Padding.Small
        )
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = Padding.Medium,
                    vertical = Padding.Small
                ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.50f),
                    text = stringResource(id = R.string.jeopardy_total_score),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.fillMaxWidth(0.50f),
                    text = state.totalScore.toString(),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = Padding.Large,
                    vertical = Padding.Small
                ),
                onClick = { handleEvent.invoke(JeopardyStatsEvent.ClearStats) }
            ) {
                Text(text = stringResource(id = R.string.jeopardy_clear))
            }
        }

        items(state.history, key = { it.question.id }) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Blue)
                    .padding(Padding.XSmall),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .clickable {
                            handleEvent.invoke(JeopardyStatsEvent.ExpandItem(item))
                        },
                    text = item.question.question,
                    maxLines = if (item == state.expandedItem) Int.MAX_VALUE else 1,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.fillMaxWidth(0.45f),
                    text = item.scoreEffect,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
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