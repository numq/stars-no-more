package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.sorting.SortingCriteria

sealed interface DashboardCommand {
    data object GetProjects : DashboardCommand

    data class SortProjects(val criteria: SortingCriteria) : DashboardCommand
}