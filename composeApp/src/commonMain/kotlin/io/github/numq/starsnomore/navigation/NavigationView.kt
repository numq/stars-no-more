package io.github.numq.starsnomore.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.numq.starsnomore.dashboard.DashboardView

@Composable
fun NavigationView(feature: NavigationFeature) {
    val state by feature.state.collectAsState()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (state is NavigationState.Dashboard) {
            DashboardView()
        }
    }
}