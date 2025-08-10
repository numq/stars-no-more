package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.sorting.SortingType

sealed interface DashboardCommand {
    data object StartLoading : DashboardCommand

    data object GetProjects : DashboardCommand

    data object RefreshProjects : DashboardCommand

    data class SortProjects(val type: SortingType) : DashboardCommand

    data object OpenContextMenu : DashboardCommand

    data object CloseContextMenu : DashboardCommand
}