package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.feature.Reducer

class ContextMenuReducer : Reducer<DashboardCommand.ContextMenu, DashboardState, DashboardEvent> {
    override suspend fun reduce(state: DashboardState, command: DashboardCommand.ContextMenu) = when (command) {
        is DashboardCommand.ContextMenu.OpenContextMenu -> when (state) {
            is DashboardState.Loading -> transition(state)

            is DashboardState.Interactable.Loaded -> transition(state.copy(isContextMenuVisible = true))

            is DashboardState.Interactable.Error -> transition(state.copy(isContextMenuVisible = true))
        }

        is DashboardCommand.ContextMenu.CloseContextMenu -> when (state) {
            is DashboardState.Loading -> transition(state)

            is DashboardState.Interactable.Loaded -> transition(state.copy(isContextMenuVisible = false))

            is DashboardState.Interactable.Error -> transition(state.copy(isContextMenuVisible = false))
        }
    }
}