package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.feature.Reducer
import io.github.numq.starsnomore.feature.Transition
import io.github.numq.starsnomore.project.GetProjects
import io.github.numq.starsnomore.sorting.SortingCriteria
import io.github.numq.starsnomore.sorting.SortingDirection
import io.github.numq.starsnomore.sorting.SortingType

class ProjectsReducer(
    private val getProjects: GetProjects,
) : Reducer<DashboardCommand.Projects, DashboardState, DashboardEvent> {
    override suspend fun reduce(
        state: DashboardState,
        command: DashboardCommand.Projects,
    ): Transition<DashboardState, DashboardEvent> = when (command) {
        is DashboardCommand.Projects.StartLoading -> transition(
            DashboardState.Loading(
                selectedSortingCriteria = state.selectedSortingCriteria,
                credentials = state.credentials,
                isCredentialsDialogVisible = state.isCredentialsDialogVisible,
                isCredentialsTokenVisible = state.isCredentialsTokenVisible,
            )
        )

        is DashboardCommand.Projects.GetProjects -> getProjects.execute(Unit).fold(onSuccess = { projects ->
            when (state) {
                is DashboardState.Loading,
                is DashboardState.Interactable.Error,
                    -> transition(
                    DashboardState.Interactable.Loaded(
                        projects = projects,
                        selectedSortingCriteria = state.selectedSortingCriteria,
                        credentials = state.credentials,
                        isCredentialsDialogVisible = state.isCredentialsDialogVisible,
                        isCredentialsTokenVisible = state.isCredentialsTokenVisible,
                        isContextMenuVisible = if (state is DashboardState.Interactable) state.isCredentialsTokenVisible else false
                    )
                )

                is DashboardState.Interactable.Loaded -> transition(state.copy(projects = projects))
            }
        }, onFailure = { t ->
            when (t) {
                is Exception -> transition(
                    DashboardState.Interactable.Error(
                        exception = t,
                        selectedSortingCriteria = state.selectedSortingCriteria,
                        credentials = state.credentials,
                        isCredentialsDialogVisible = state.isCredentialsDialogVisible,
                        isCredentialsTokenVisible = state.isCredentialsTokenVisible,
                        isContextMenuVisible = if (state is DashboardState.Interactable) state.isCredentialsTokenVisible else false
                    )
                )

                else -> transition(
                    DashboardState.Interactable.Error(
                        exception = Exception(t),
                        selectedSortingCriteria = state.selectedSortingCriteria,
                        credentials = state.credentials,
                        isCredentialsDialogVisible = state.isCredentialsDialogVisible,
                        isCredentialsTokenVisible = state.isCredentialsTokenVisible,
                        isContextMenuVisible = if (state is DashboardState.Interactable) state.isCredentialsTokenVisible else false
                    )
                )
            }
        })

        is DashboardCommand.Projects.RefreshProjects -> reduce(state, DashboardCommand.Projects.GetProjects)

        is DashboardCommand.Projects.SortProjects -> with(command) {
            when (state) {
                is DashboardState.Interactable.Loaded -> {
                    val criteria = state.selectedSortingCriteria

                    val direction = when (type) {
                        criteria.type -> when (criteria.direction) {
                            SortingDirection.ASCENDING -> SortingDirection.DESCENDING

                            SortingDirection.DESCENDING -> SortingDirection.ASCENDING
                        }

                        else -> criteria.direction
                    }

                    val updatedCriteria = when (type) {
                        SortingType.NAME -> SortingCriteria.Name(direction = direction)

                        SortingType.STARGAZERS -> SortingCriteria.Stargazers(direction = direction)

                        SortingType.FORKS -> SortingCriteria.Forks(direction = direction)

                        SortingType.CLONES -> SortingCriteria.Traffic.Clones(direction = direction)

                        SortingType.CLONERS -> SortingCriteria.Traffic.Cloners(direction = direction)

                        SortingType.VIEWS -> SortingCriteria.Traffic.Views(direction = direction)

                        SortingType.VISITORS -> SortingCriteria.Traffic.Visitors(direction = direction)

                        SortingType.CREATED_AT -> SortingCriteria.Date.CreatedAt(direction = direction)

                        SortingType.PUSHED_AT -> SortingCriteria.Date.PushedAt(direction = direction)
                    }

                    transition(state.copy(projects = state.projects, selectedSortingCriteria = updatedCriteria))
                }

                else -> transition(state)
            }
        }
    }
}