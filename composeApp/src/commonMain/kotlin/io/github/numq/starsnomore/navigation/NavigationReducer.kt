package io.github.numq.starsnomore.navigation

import io.github.numq.starsnomore.feature.Reducer

class NavigationReducer : Reducer<NavigationCommand, NavigationState, NavigationEvent> {
    override suspend fun reduce(state: NavigationState, command: NavigationCommand) = when (command) {
        is NavigationCommand.NavigateToDashboard -> transition(NavigationState.Dashboard)
    }
}