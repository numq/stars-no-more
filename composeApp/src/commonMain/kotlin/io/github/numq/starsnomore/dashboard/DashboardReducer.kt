package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.feature.Reducer
import io.github.numq.starsnomore.feature.Transition
import io.github.numq.starsnomore.project.GetProjects
import io.github.numq.starsnomore.project.Project
import io.github.numq.starsnomore.project.ProjectComparator
import io.github.numq.starsnomore.sorting.SortingCriteria

class DashboardReducer(
    private val getProjects: GetProjects,
) : Reducer<DashboardCommand, DashboardState, DashboardEvent> {
    private fun sortedProjects(projects: List<Project>, criteria: SortingCriteria, isAscending: Boolean) =
        projects.sortedWith(ProjectComparator.create(criteria, isAscending))

    override suspend fun reduce(
        state: DashboardState,
        command: DashboardCommand,
    ): Transition<DashboardState, DashboardEvent> = when (command) {
        is DashboardCommand.UpdateLoadingState -> transition(state.copy(isLoadingProjects = command.isLoadingProjects))

        is DashboardCommand.GetProjects -> getProjects.execute(Unit).fold(onSuccess = { projects ->
            transition(
                state.copy(
                    projects = sortedProjects(
                        projects = projects, criteria = state.selectedSortingCriteria, isAscending = state.isAscending
                    ), isLoadingProjects = false
                )
            )
        }, onFailure = { t ->
            transition(state.copy(isLoadingProjects = false), DashboardEvent.Error(Exception(t.localizedMessage)))
        })

        is DashboardCommand.SortProjects -> with(command) {
            val isAscending = if (criteria == state.selectedSortingCriteria) !state.isAscending else state.isAscending

            transition(
                state.copy(
                    projects = sortedProjects(
                        projects = state.projects, criteria = criteria, isAscending = isAscending
                    ), selectedSortingCriteria = criteria, isAscending = isAscending
                )
            )
        }
    }
}