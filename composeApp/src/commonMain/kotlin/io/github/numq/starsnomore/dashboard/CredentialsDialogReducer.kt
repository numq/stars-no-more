package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.credentials.GetCredentials
import io.github.numq.starsnomore.credentials.UpdateCredentials
import io.github.numq.starsnomore.feature.Reducer
import io.github.numq.starsnomore.feature.Transition
import io.github.numq.starsnomore.feature.mergeEvents

class CredentialsDialogReducer(
    private val getCredentials: GetCredentials,
    private val updateCredentials: UpdateCredentials,
    private val projectsReducer: ProjectsReducer,
) : Reducer<DashboardCommand.CredentialsDialog, DashboardState, DashboardEvent> {
    override suspend fun reduce(
        state: DashboardState,
        command: DashboardCommand.CredentialsDialog,
    ): Transition<DashboardState, DashboardEvent> = when (command) {
        is DashboardCommand.CredentialsDialog.GetCredentials -> getCredentials.execute(Unit)
            .fold(onSuccess = { credentials ->
                val updatedState = when (state) {
                    is DashboardState.Loading -> state

                    is DashboardState.Interactable.Loaded -> state.copy(credentials = credentials)

                    is DashboardState.Interactable.Error -> state.copy(credentials = credentials)
                }

                transition(updatedState)
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

        is DashboardCommand.CredentialsDialog.OpenCredentialsDialog -> {
            val updatedState = when (state) {
                is DashboardState.Loading -> state.copy(
                    isCredentialsDialogVisible = true
                )

                is DashboardState.Interactable.Loaded -> state.copy(
                    isCredentialsDialogVisible = true
                )

                is DashboardState.Interactable.Error -> state.copy(
                    isCredentialsDialogVisible = true
                )
            }

            reduce(updatedState, DashboardCommand.CredentialsDialog.GetCredentials)
        }

        is DashboardCommand.CredentialsDialog.UpdateCredentialsDialog -> updateCredentials.execute(
            input = UpdateCredentials.Input(
                credentials = command.credentials
            )
        ).fold(onSuccess = {
            val (updatedState, events) = reduce(state, DashboardCommand.CredentialsDialog.CloseCredentialsDialog)

            projectsReducer.reduce(updatedState, DashboardCommand.Projects.RefreshProjects).mergeEvents(events)
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

        is DashboardCommand.CredentialsDialog.CloseCredentialsDialog -> {
            val updatedState = when (state) {
                is DashboardState.Loading -> state.copy(isCredentialsDialogVisible = false)

                is DashboardState.Interactable.Loaded -> state.copy(isCredentialsDialogVisible = false)

                is DashboardState.Interactable.Error -> state.copy(isCredentialsDialogVisible = false)
            }

            transition(updatedState)
        }

        is DashboardCommand.CredentialsDialog.ToggleTokenVisibility -> {
            val isVisible = !state.isCredentialsTokenVisible

            val updatedState = when (state) {
                is DashboardState.Loading -> state.copy(isCredentialsTokenVisible = isVisible)

                is DashboardState.Interactable.Loaded -> state.copy(isCredentialsTokenVisible = isVisible)

                is DashboardState.Interactable.Error -> state.copy(isCredentialsTokenVisible = isVisible)
            }

            transition(updatedState)
        }
    }
}