package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.credentials.CredentialsDialogControls
import io.github.numq.starsnomore.feature.Feature
import kotlinx.coroutines.*
import org.koin.ext.getFullName

class DashboardFeature(reducer: DashboardReducer) : Feature<DashboardCommand, DashboardState, DashboardEvent>(
    initialState = DashboardState.Loading(),
    coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
    reducer = reducer
) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private val jobs = mutableMapOf<String, Job>()

    init {
        coroutineScope.launch {
            events.collect { event ->
                val key = event::class.getFullName()

                jobs[key]?.cancel()

                jobs[key] = launch { execute(DashboardCommand.Projects.RefreshProjects) }
            }
        }

        CredentialsDialogControls.open = {
            coroutineScope.launch {
                execute(DashboardCommand.CredentialsDialog.OpenCredentialsDialog)
            }
        }

        coroutineScope.launch {
            execute(DashboardCommand.Projects.GetProjects)
        }

        invokeOnClose {
            coroutineScope.cancel()
        }
    }
}