package io.github.numq.starsnomore.application

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import io.github.numq.starsnomore.credentials.CredentialsInputDialog
import io.github.numq.starsnomore.credentials.CredentialsManager
import io.github.numq.starsnomore.dashboard.DashboardCommand
import io.github.numq.starsnomore.dashboard.DashboardFeature
import io.github.numq.starsnomore.decoration.WindowDecoration
import io.github.numq.starsnomore.decoration.WindowDecorationColors
import io.github.numq.starsnomore.di.appModule
import io.github.numq.starsnomore.navigation.NavigationView
import io.github.numq.starsnomore.theme.StarsNoMoreTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.startKoin

private const val APP_NAME = "Stars Not More"

private val minimumWindowSize = DpSize(900.dp, 600.dp)

fun main() {
    startKoin { modules(appModule) }

    application {
        val coroutineScope = rememberCoroutineScope()

        val isSystemInDarkTheme = isSystemInDarkTheme()

        val (isDarkTheme, setIsDarkTheme) = remember(isSystemInDarkTheme) {
            mutableStateOf(isSystemInDarkTheme)
        }

        val dashboardFeature = koinInject<DashboardFeature>()

        val credentialsManager = koinInject<CredentialsManager>()

        val credentials by credentialsManager.credentials.collectAsState()

        var isCredentialsDialogVisible by remember { mutableStateOf(false) }

        StarsNoMoreTheme(isDarkTheme = isDarkTheme) {
            WindowDecoration(
                isDarkTheme = isDarkTheme,
                setIsDarkTheme = setIsDarkTheme,
                initialWindowSize = minimumWindowSize,
                minimumWindowSize = minimumWindowSize,
                windowDecorationColors = WindowDecorationColors(switchSchemeButton = { Color.Unspecified }),
                title = {
                    Text(APP_NAME, color = MaterialTheme.colorScheme.primary)
                },
                controls = {
                    Box(
                        modifier = Modifier.aspectRatio(1f).clickable {
                            isCredentialsDialogVisible = true
                        }, contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AccountBox, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                content = {
                    NavigationView(feature = koinInject())

                    if (isCredentialsDialogVisible) {
                        CredentialsInputDialog(credentials = credentials, onDismissRequest = {
                            isCredentialsDialogVisible = false
                        }, onCredentialsSubmit = { credentials ->
                            credentialsManager.updateCredentials(credentials = credentials).onSuccess {
                                coroutineScope.launch {
                                    dashboardFeature.execute(DashboardCommand.RefreshProjects)
                                }
                            }

                            isCredentialsDialogVisible = false
                        })
                    }
                })
        }
    }
}