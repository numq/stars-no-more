package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.credentials.Credentials
import io.github.numq.starsnomore.project.Project
import io.github.numq.starsnomore.project.ProjectComparator
import io.github.numq.starsnomore.sorting.SortingCriteria
import io.github.numq.starsnomore.sorting.SortingDirection

sealed interface DashboardState {
    val selectedSortingCriteria: SortingCriteria

    val credentials: Credentials

    val isCredentialsDialogVisible: Boolean

    val isCredentialsTokenVisible: Boolean

    data class Loading(
        override val selectedSortingCriteria: SortingCriteria = SortingCriteria.Date.PushedAt(direction = SortingDirection.DESCENDING),
        override val credentials: Credentials = Credentials(),
        override val isCredentialsDialogVisible: Boolean = false,
        override val isCredentialsTokenVisible: Boolean = false,
    ) : DashboardState

    sealed interface Interactable : DashboardState {
        val isContextMenuVisible: Boolean

        data class Loaded(
            val projects: List<Project> = emptyList(),
            override val selectedSortingCriteria: SortingCriteria,
            override val credentials: Credentials,
            override val isCredentialsDialogVisible: Boolean,
            override val isCredentialsTokenVisible: Boolean,
            override val isContextMenuVisible: Boolean,
        ) : Interactable {
            val sortedProjects = projects.sortedWith(ProjectComparator.create(selectedSortingCriteria))
        }

        data class Error(
            val exception: Exception,
            override val selectedSortingCriteria: SortingCriteria,
            override val credentials: Credentials,
            override val isCredentialsDialogVisible: Boolean,
            override val isCredentialsTokenVisible: Boolean,
            override val isContextMenuVisible: Boolean,
        ) : Interactable
    }
}