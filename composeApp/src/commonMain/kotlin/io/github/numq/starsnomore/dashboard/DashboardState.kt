package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.project.Project
import io.github.numq.starsnomore.project.ProjectComparator
import io.github.numq.starsnomore.sorting.SortingCriteria
import io.github.numq.starsnomore.sorting.SortingDirection

sealed interface DashboardState {
    val selectedSortingCriteria: SortingCriteria

    data class Loading(
        override val selectedSortingCriteria: SortingCriteria = SortingCriteria.Date.PushedAt(direction = SortingDirection.DESCENDING),
    ) : DashboardState

    sealed interface Interactable : DashboardState {
        val isContextMenuVisible: Boolean

        data class Loaded(
            val projects: List<Project> = emptyList(),
            override val selectedSortingCriteria: SortingCriteria,
            override val isContextMenuVisible: Boolean = false,
        ) : Interactable {
            val sortedProjects = projects.sortedWith(ProjectComparator.create(selectedSortingCriteria))
        }

        data class Error(
            val exception: Exception,
            override val selectedSortingCriteria: SortingCriteria,
            override val isContextMenuVisible: Boolean = false,
        ) : Interactable
    }
}