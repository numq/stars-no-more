package io.github.numq.starsnomore.di

import io.github.numq.starsnomore.credentials.CredentialsDialogControls
import io.github.numq.starsnomore.credentials.CredentialsManager
import io.github.numq.starsnomore.credentials.GetCredentials
import io.github.numq.starsnomore.credentials.UpdateCredentials
import io.github.numq.starsnomore.dashboard.*
import io.github.numq.starsnomore.navigation.NavigationFeature
import io.github.numq.starsnomore.navigation.NavigationReducer
import io.github.numq.starsnomore.project.*
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.onClose

private val application = module {
    single {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
        }
    } bind HttpClient::class onClose { it?.close() }
}

private val navigation = module {
    factory { NavigationReducer() }
    single { NavigationFeature(get()) } onClose { it?.close() }
}

private val credentials = module {
    single { CredentialsDialogControls }
    single { CredentialsManager.Default(get()) } bind CredentialsManager::class
    factory { GetCredentials(get()) }
    factory { UpdateCredentials(get()) }
}

private val project = module {
    single { GitHubProjectService(get(), get(), get()) } bind ProjectService::class
    single { GitHubProjectRepository(get()) } bind ProjectRepository::class
    factory { GetProjects(get()) }
}

private val dashboard = module {
    factory { ContextMenuReducer() }
    factory { CredentialsDialogReducer(get(), get(), get()) }
    factory { ProjectsReducer(get()) }
    factory { DashboardReducer(get(), get(), get()) }
    single { DashboardFeature(get()) } onClose { it?.close() }
}

internal val appModule = listOf(application, navigation, credentials, project, dashboard)