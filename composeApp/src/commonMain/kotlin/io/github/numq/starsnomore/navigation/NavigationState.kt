package io.github.numq.starsnomore.navigation

sealed interface NavigationState {
    data object Dashboard : NavigationState
}