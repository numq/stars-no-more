package com.github.numq.repo

import kotlinx.serialization.Serializable

@Serializable
data class GithubClones(
    val count: Int = 0,
    val uniques: Int = 0,
)