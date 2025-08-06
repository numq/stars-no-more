package io.github.numq.starsnomore.project

import io.github.numq.starsnomore.growth.Growth
import kotlin.time.Duration

data class Project(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val url: String,
    val stargazers: Int,
    val forks: Int,
    val clonesGrowth: Growth<Int>,
    val clonersGrowth: Growth<Int>,
    val viewsGrowth: Growth<Int>,
    val visitorsGrowth: Growth<Int>,
    val createdAt: Duration,
    val pushedAt: Duration,
)