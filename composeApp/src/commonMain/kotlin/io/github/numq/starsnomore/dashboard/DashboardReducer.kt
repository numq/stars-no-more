package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.feature.Reducer
import io.github.numq.starsnomore.feature.Transition

class DashboardReducer(
    private val contextMenuReducer: ContextMenuReducer,
    private val credentialsDialogReducer: CredentialsDialogReducer,
    private val projectsReducer: ProjectsReducer,
) : Reducer<DashboardCommand, DashboardState, DashboardEvent> {
    override suspend fun reduce(
        state: DashboardState,
        command: DashboardCommand,
    ): Transition<DashboardState, DashboardEvent> = when (command) {
        is DashboardCommand.ContextMenu -> contextMenuReducer.reduce(state, command)

        is DashboardCommand.CredentialsDialog -> credentialsDialogReducer.reduce(state, command)

        is DashboardCommand.Projects -> projectsReducer.reduce(state, command)
    }
}