package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.credentials.Credentials
import io.github.numq.starsnomore.sorting.SortingType

sealed interface DashboardCommand {
    sealed interface ContextMenu : DashboardCommand {
        data object OpenContextMenu : ContextMenu

        data object CloseContextMenu : ContextMenu
    }

    sealed interface CredentialsDialog : DashboardCommand {
        data object GetCredentials : CredentialsDialog

        data object OpenCredentialsDialog : CredentialsDialog

        data class UpdateCredentialsDialog(val credentials: Credentials) : CredentialsDialog

        data object CloseCredentialsDialog : CredentialsDialog

        data object ToggleTokenVisibility : CredentialsDialog
    }

    sealed interface Projects : DashboardCommand {
        data object StartLoading : Projects

        data object GetProjects : Projects

        data object RefreshProjects : Projects

        data class SortProjects(val type: SortingType) : Projects
    }

}