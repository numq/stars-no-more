package io.github.numq.starsnomore.navigation

import io.github.numq.starsnomore.feature.Feature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class NavigationFeature(reducer: NavigationReducer) : Feature<NavigationCommand, NavigationState, NavigationEvent>(
    initialState = NavigationState.Dashboard,
    coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
    reducer = reducer
)