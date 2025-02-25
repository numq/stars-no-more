package com.github.numq.repo

import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GithubRepoService(
    private val username: String,
    private val token: String,
    private val json: Json,
    private val client: HttpClient,
) : RepoService {
    init {
        require(username.isNotBlank()) { "Username should not be empty" }
    }

    private companion object {
        const val BASE_URL = "https://api.github.com"
        const val REPOS_PER_PAGE = 15
        const val ENDPOINT_REPOSITORIES = "repos"
        const val ENDPOINT_CLONES = "traffic/clones"
        const val ENDPOINT_VIEWS = "traffic/views"
    }

    private fun buildRequest(endpoint: String) = HttpRequest.newBuilder()
        .uri(URI.create("$BASE_URL/$endpoint"))
        .header("Accept", "application/vnd.github+json")
        .apply {
            if (token.isNotBlank()) header("Authorization", "Bearer $token")
        }
        .header("X-GitHub-Api-Version", "2022-11-28")
        .GET()
        .build()

    override suspend fun getRepos(page: Int) = runCatching {
        val request = buildRequest(
            "users/$username/$ENDPOINT_REPOSITORIES?sort=pushed&per_page=$REPOS_PER_PAGE&page=$page"
        )

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() == 200) {
            json.decodeFromString<List<GithubRepo>>(response.body())
        } else {
            throw Exception("Failed to fetch repositories: ${response.statusCode()} - ${response.body()}")
        }
    }

    override suspend fun getClones(githubRepo: GithubRepo) = runCatching {
        val request = buildRequest("repos/$username/${githubRepo.name}/$ENDPOINT_CLONES")

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() == 200) {
            json.decodeFromString<GithubClones>(response.body())
        } else {
            throw Exception("Failed to fetch clones: ${response.statusCode()} - ${response.body()}")
        }
    }

    override suspend fun getViews(githubRepo: GithubRepo) = runCatching {
        val request = buildRequest("repos/$username/${githubRepo.name}/$ENDPOINT_VIEWS")

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() == 200) {
            json.decodeFromString<GithubViews>(response.body())
        } else {
            throw Exception("Failed to fetch views: ${response.statusCode()} - ${response.body()}")
        }
    }
}