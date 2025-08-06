package io.github.numq.starsnomore.project

import io.github.numq.starsnomore.trend.Trend
import kotlin.time.Duration

data class Project(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val url: String,
    val stargazers: Int,
    val forks: Int,
    val clonesTrend: Trend<Int>,
    val clonersTrend: Trend<Int>,
    val viewsTrend: Trend<Int>,
    val visitorsTrend: Trend<Int>,
    val createdAt: Duration,
    val pushedAt: Duration,
)