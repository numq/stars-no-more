package io.github.numq.starsnomore.project

import io.github.numq.starsnomore.credentials.Credentials
import io.github.numq.starsnomore.credentials.CredentialsException
import io.github.numq.starsnomore.credentials.CredentialsManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class GitHubProjectService(
    private val json: Json,
    private val client: HttpClient,
    private val credentialsManager: CredentialsManager,
) : ProjectService {
    private companion object {
        const val BASE_URL = "https://api.github.com"
        const val ENDPOINT_CLONES = "traffic/clones"
        const val ENDPOINT_VIEWS = "traffic/views"
        const val TYPE = "owner"
        const val PER_PAGE = 30
        const val PAGE = 1
        const val SORT = "pushed"
        const val DIRECTION = "desc"
    }

    private fun requireCredentials(credentials: Credentials) = with(credentials) {
        when {
            credentials.name.isBlank() -> throw CredentialsException("Bad credentials: Name should not be empty")

            credentials.token.isBlank() -> throw CredentialsException("Bad credentials: Token should not be empty")
        }

        this
    }

    private fun HttpRequestBuilder.githubHeaders(token: String) {
        header("Accept", "application/vnd.github+json")
        header("X-GitHub-Api-Version", "2022-11-28")
        if (token.isNotBlank()) {
            header("Authorization", "Bearer $token")
        }
    }

    override suspend fun getProjects() = runCatching {
        withContext(Dispatchers.IO) {
            val (name, token) = requireCredentials(credentialsManager.credentials.value)

            val url =
                "$BASE_URL/users/$name/repos?type=$TYPE&per_page=$PER_PAGE&page=$PAGE&sort=$SORT&direction=$DIRECTION"

            client.get(url) {
                githubHeaders(token)
            }.let { response ->
                when {
                    response.status.isSuccess() -> response.body<List<GitHubProject>>()
                        .filterNot(GitHubProject::isPrivate)

                    else -> throw ProjectServiceException(
                        message = "Failed to fetch repositories",
                        status = response.status.value,
                        body = json.decodeFromString<Map<String, String>>(response.bodyAsText())["message"]
                    )
                }
            }
        }
    }

    override suspend fun getCloneTraffic(githubProject: GitHubProject) = runCatching {
        withContext(Dispatchers.IO) {
            val token = requireCredentials(credentialsManager.credentials.value).token

            val url = "$BASE_URL/repos/${githubProject.fullName}/$ENDPOINT_CLONES"

            client.get(url) {
                githubHeaders(token = token)
            }.let { response ->
                when {
                    response.status.isSuccess() -> response.body<GitHubClones>()

                    else -> throw ProjectServiceException(
                        message = "Failed to fetch clones",
                        status = response.status.value,
                        body = json.decodeFromString<Map<String, String>>(response.bodyAsText())["message"]
                    )
                }
            }
        }
    }

    override suspend fun getViewTraffic(githubProject: GitHubProject) = runCatching {
        withContext(Dispatchers.IO) {
            val token = requireCredentials(credentialsManager.credentials.value).token

            require(token.isNotBlank()) { "Bad credentials: Token should not be empty" }

            val url = "$BASE_URL/repos/${githubProject.fullName}/$ENDPOINT_VIEWS"

            client.get(url) {
                githubHeaders(token = token)
            }.let { response ->
                when {
                    response.status.isSuccess() -> response.body<GitHubViews>()

                    else -> throw ProjectServiceException(
                        message = "Failed to fetch views",
                        status = response.status.value,
                        body = json.decodeFromString<Map<String, String>>(response.bodyAsText())["message"]
                    )
                }
            }
        }
    }
}