package com.github.numq

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.numq.repo.GetRepos
import com.github.numq.repo.GithubRepoService
import com.github.numq.repo.RepoRepository
import com.github.numq.repo.RepoService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.net.http.HttpClient

fun main() {
    val client = HttpClient.newHttpClient()

    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val credentials = File("${File(System.getProperty("user.dir")).parent}/credentials.json")

    require(credentials.exists()) { "Not found credentials.json" }

    val (username, token) = with(Json.parseToJsonElement(credentials.readText()).jsonObject) {
        Pair(
            get("username")?.jsonPrimitive?.content ?: "",
            get("token")?.jsonPrimitive?.content ?: "",
        )
    }

    val repoService: RepoService = GithubRepoService(username = username, token = token, json = json, client = client)

    val repoRepository = RepoRepository.create(repoService)

    val getRepos = GetRepos(repoRepository)

    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "github-stats",
        ) {
            MaterialTheme {
                App(getRepos = getRepos)
            }
        }
    }
}