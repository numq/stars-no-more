package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.feature.Reducer
import io.github.numq.starsnomore.feature.Transition
import io.github.numq.starsnomore.project.GetProjects
import io.github.numq.starsnomore.project.Project
import io.github.numq.starsnomore.sorting.SortingCriteria

class DashboardReducer(
    private val getProjects: GetProjects,
) : Reducer<DashboardCommand, DashboardState, DashboardEvent> {
    private fun getSortingFunction(criteria: SortingCriteria, isAscending: Boolean): (Project) -> Comparable<*> {
        val selector: (Project) -> Comparable<*> = when (criteria) {
            SortingCriteria.Name -> Project::name

            SortingCriteria.Stargazers -> Project::stargazers

            SortingCriteria.Forks -> Project::forks

            SortingCriteria.Traffic.Clones -> { it -> it.clonesTrend.value }

            SortingCriteria.Traffic.Cloners -> { it -> it.clonersTrend.value }

            SortingCriteria.Traffic.Views -> { it -> it.viewsTrend.value }

            SortingCriteria.Traffic.Visitors -> { it -> it.visitorsTrend.value }

            SortingCriteria.Date.CreatedAt -> { it -> it.createdAt.inWholeMilliseconds }

            SortingCriteria.Date.PushedAt -> { it -> it.pushedAt.inWholeMilliseconds }
        }

        return if (isAscending) selector else { p ->
            when (val v = selector(p)) {
                is String -> v.reversed()

                is Number -> -v.toDouble()

                else -> v
            }
        }
    }

    override suspend fun reduce(
        state: DashboardState,
        command: DashboardCommand,
    ): Transition<DashboardState, DashboardEvent> = when (command) {
        is DashboardCommand.GetProjects -> getProjects.execute(Unit).fold(onSuccess = { projects ->
            reduce(
                state.copy(projects = projects),
                DashboardCommand.SortProjects(criteria = state.selectedSortingCriteria)
            )
        }, onFailure = { t ->
            transition(state, DashboardEvent.Error(Exception(t.localizedMessage)))
        })

        is DashboardCommand.SortProjects -> with(command) {
            val isAscending = if (criteria == state.selectedSortingCriteria) !state.isAscending else state.isAscending

            transition(
                state.copy(
                    projects = state.projects.sortedWith(compareBy(getSortingFunction(criteria, isAscending))),
                    selectedSortingCriteria = criteria,
                    isAscending = isAscending
                )
            )
        }
    }
}