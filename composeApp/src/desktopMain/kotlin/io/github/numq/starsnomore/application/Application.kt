package io.github.numq.starsnomore.application

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import io.github.numq.starsnomore.decoration.WindowDecoration
import io.github.numq.starsnomore.decoration.WindowDecorationColors
import io.github.numq.starsnomore.di.appModule
import io.github.numq.starsnomore.navigation.NavigationView
import io.github.numq.starsnomore.theme.StarsNoMoreTheme
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.startKoin

private const val APP_NAME = "Stars Not More"

private val minimumWindowSize = DpSize(900.dp, 600.dp)

fun main() {
    startKoin { modules(appModule) }

    application {
        val isSystemInDarkTheme = isSystemInDarkTheme()

        val (isDarkTheme, setIsDarkTheme) = remember(isSystemInDarkTheme) {
            mutableStateOf(isSystemInDarkTheme)
        }

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
                content = {
                    NavigationView(feature = koinInject())
                })
        }
    }
}