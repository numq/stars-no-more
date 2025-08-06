package io.github.numq.starsnomore.di

import io.github.numq.starsnomore.dashboard.DashboardFeature
import io.github.numq.starsnomore.dashboard.DashboardReducer
import io.github.numq.starsnomore.navigation.NavigationFeature
import io.github.numq.starsnomore.navigation.NavigationReducer
import io.github.numq.starsnomore.project.*
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.onClose
import java.io.File

private val application = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    } bind HttpClient::class onClose { it?.close() }
}

private val navigation = module {
    single { NavigationReducer() }
    single { NavigationFeature(get()) } onClose { it?.close() }
}

private val project = module {
    single {
        // todo

        val credentials = File("${File(System.getProperty("user.dir")).parent}/credentials.json")

        require(credentials.exists()) { "Not found credentials.json" }

        val (username, token) = with(Json.parseToJsonElement(credentials.readText()).jsonObject) {
            Pair(
                get("username")?.jsonPrimitive?.content ?: "",
                get("token")?.jsonPrimitive?.content ?: "",
            )
        }

        GitHubProjectService(get(), username, token)
    } bind ProjectService::class
    single { GitHubProjectRepository(get()) } bind ProjectRepository::class
    single { GetProjects(get()) }
}

private val dashboard = module {
    single { DashboardReducer(get()) }
    single { DashboardFeature(get()) } onClose { it?.close() }
}

internal val appModule = listOf(application, navigation, project, dashboard)