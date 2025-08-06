package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.project.Project
import io.github.numq.starsnomore.sorting.SortingCriteria
import kotlin.time.Duration

data class DashboardState(
    val projects: List<Project> = emptyList(),
    val updateTime: Duration? = null,
    val selectedSortingCriteria: SortingCriteria = SortingCriteria.Date.PushedAt,
    val isAscending: Boolean = false,
)