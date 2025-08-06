package io.github.numq.starsnomore.project

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

internal class GitHubProjectService(
    private val client: HttpClient,
    private val username: String,
    private val token: String,
) : ProjectService {
    init {
        require(username.isNotBlank()) { "Username should not be empty" }

        require(token.isNotBlank()) { "Token should not be empty" }
    }

    private companion object {
        const val BASE_URL = "https://api.github.com"
        const val REPOS_PER_PAGE = 15
        const val ENDPOINT_REPOSITORIES = "repos"
        const val ENDPOINT_CLONES = "traffic/clones"
        const val ENDPOINT_VIEWS = "traffic/views"
    }

    private fun HttpRequestBuilder.githubHeaders() {
        header("Accept", "application/vnd.github+json")
        header("X-GitHub-Api-Version", "2022-11-28")
        if (token.isNotBlank()) {
            header("Authorization", "Bearer $token")
        }
    }

    override suspend fun getProjects(page: Int) = runCatching {
        val url = "$BASE_URL/users/$username/$ENDPOINT_REPOSITORIES?sort=pushed&per_page=$REPOS_PER_PAGE&page=$page"

        client.get(url) {
            githubHeaders()
        }.let { response ->
            when {
                response.status.isSuccess() -> response.body<List<GitHubProject>>().filterNot(GitHubProject::isPrivate)

                else -> throw Exception("Failed to fetch repositories: ${response.status} - ${response.bodyAsText()}")
            }
        }
    }

    override suspend fun getCloneTraffic(githubProject: GitHubProject) = runCatching {
        val url = "$BASE_URL/repos/${githubProject.fullName}/$ENDPOINT_CLONES"

        client.get(url) {
            githubHeaders()
        }.let { response ->
            when {
                response.status.isSuccess() -> response.body<GitHubClones>()

                else -> throw Exception("Failed to fetch clones: ${response.status} - ${response.bodyAsText()}")
            }
        }
    }

    override suspend fun getViewTraffic(githubProject: GitHubProject) = runCatching {
        val url = "$BASE_URL/repos/${githubProject.fullName}/$ENDPOINT_VIEWS"

        client.get(url) {
            githubHeaders()
        }.let { response ->
            when {
                response.status.isSuccess() -> response.body<GitHubViews>()

                else -> throw Exception("Failed to fetch views: ${response.status} - ${response.bodyAsText()}")
            }
        }
    }
}