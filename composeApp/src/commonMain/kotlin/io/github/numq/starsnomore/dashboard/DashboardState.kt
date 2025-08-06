package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.project.Project
import io.github.numq.starsnomore.sorting.SortingCriteria

data class DashboardState(
    val projects: List<Project> = emptyList(),
    val isLoadingProjects: Boolean = true,
    val selectedSortingCriteria: SortingCriteria = SortingCriteria.Date.PushedAt,
    val isAscending: Boolean = false,
)