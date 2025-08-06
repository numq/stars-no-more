package io.github.numq.starsnomore.project

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubProject(
    val id: Long,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val description: String? = null,
    @SerialName("html_url") val url: String,
    @SerialName("stargazers_count") val stargazers: Int = 0,
    @SerialName("forks_count") val forks: Int = 0,
    @SerialName("private") val isPrivate: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("pushed_at") val pushedAt: String,
)