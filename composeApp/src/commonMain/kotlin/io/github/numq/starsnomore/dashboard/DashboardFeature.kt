package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.feature.Feature
import kotlinx.coroutines.*

class DashboardFeature(reducer: DashboardReducer) : Feature<DashboardCommand, DashboardState, DashboardEvent>(
    initialState = DashboardState.Loading(),
    coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
    reducer = reducer
) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        coroutineScope.launch {
            execute(DashboardCommand.GetProjects)
        }

        invokeOnClose {
            coroutineScope.cancel()
        }
    }
}