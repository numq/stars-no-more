package io.github.numq.starsnomore.project

import kotlinx.serialization.Serializable

@Serializable
data class GitHubDailyTraffic(
    val timestamp: String,
    val count: Int,
    val uniques: Int,
)