package io.github.numq.starsnomore.navigation

sealed interface NavigationCommand {
    data object NavigateToDashboard : NavigationCommand
}