package io.github.numq.starsnomore.project

import kotlinx.serialization.Serializable

@Serializable
data class GitHubClones(
    val count: Int = 0,
    val uniques: Int = 0,
    val clones: List<GitHubDailyTraffic> = emptyList(),
)