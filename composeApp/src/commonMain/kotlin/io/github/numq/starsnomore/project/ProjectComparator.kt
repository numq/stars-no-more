package io.github.numq.starsnomore.project

import io.github.numq.starsnomore.sorting.SortingCriteria

object ProjectComparator {
    private inline fun <T : Comparable<T>> compareByDirection(
        isAscending: Boolean,
        crossinline selector: (Project) -> T,
    ) = when {
        isAscending -> compareBy(selector)

        else -> compareByDescending(selector)
    }

    fun create(criteria: SortingCriteria, isAscending: Boolean) = when (criteria) {
        SortingCriteria.Name -> compareByDirection(isAscending, Project::name)

        SortingCriteria.Stargazers -> compareByDirection(isAscending, Project::stargazers)

        SortingCriteria.Forks -> compareByDirection(isAscending, Project::forks)

        SortingCriteria.Traffic.Clones -> compareByDirection(isAscending) { it.clonesGrowth.value }

        SortingCriteria.Traffic.Cloners -> compareByDirection(isAscending) { it.clonersGrowth.value }

        SortingCriteria.Traffic.Views -> compareByDirection(isAscending) { it.viewsGrowth.value }

        SortingCriteria.Traffic.Visitors -> compareByDirection(isAscending) { it.visitorsGrowth.value }

        SortingCriteria.Date.CreatedAt -> compareByDirection(isAscending) { it.createdAt.inWholeMilliseconds }

        SortingCriteria.Date.PushedAt -> compareByDirection(isAscending) { it.pushedAt.inWholeMilliseconds }
    }
}