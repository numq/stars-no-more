package io.github.numq.starsnomore.project

import io.github.numq.starsnomore.sorting.SortingCriteria
import io.github.numq.starsnomore.sorting.SortingDirection

object ProjectComparator {
    private inline fun <T : Comparable<T>> compareByDirection(
        direction: SortingDirection,
        crossinline selector: (Project) -> T,
    ) = when (direction) {
        SortingDirection.ASCENDING -> compareBy(selector)

        SortingDirection.DESCENDING -> compareByDescending(selector)
    }

    fun create(criteria: SortingCriteria) = when (criteria) {
        is SortingCriteria.Name -> compareByDirection(criteria.direction, Project::name)

        is SortingCriteria.Stargazers -> compareByDirection(criteria.direction, Project::stargazers)

        is SortingCriteria.Forks -> compareByDirection(criteria.direction, Project::forks)

        is SortingCriteria.Traffic.Clones -> compareByDirection(criteria.direction) { it.clonesGrowth.value }

        is SortingCriteria.Traffic.Cloners -> compareByDirection(criteria.direction) { it.clonersGrowth.value }

        is SortingCriteria.Traffic.Views -> compareByDirection(criteria.direction) { it.viewsGrowth.value }

        is SortingCriteria.Traffic.Visitors -> compareByDirection(criteria.direction) { it.visitorsGrowth.value }

        is SortingCriteria.Date.CreatedAt -> compareByDirection(criteria.direction) { it.createdAt.inWholeMilliseconds }

        is SortingCriteria.Date.PushedAt -> compareByDirection(criteria.direction) { it.pushedAt.inWholeMilliseconds }
    }
}