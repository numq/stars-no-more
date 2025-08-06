package io.github.numq.starsnomore.project

import kotlinx.serialization.Serializable

@Serializable
data class GitHubViews(
    val count: Int = 0,
    val uniques: Int = 0,
    val views: List<GitHubDailyTraffic> = emptyList(),
)