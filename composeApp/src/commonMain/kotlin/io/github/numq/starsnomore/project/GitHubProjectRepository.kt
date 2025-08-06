package io.github.numq.starsnomore.project

import io.github.numq.starsnomore.trend.Trend
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

internal class GitHubProjectRepository(private val projectService: ProjectService) : ProjectRepository {
    private companion object {
        const val FIRST_PAGE = 1
        const val WEEKLY_HISTORY = 7
    }

    private fun calculateTrend(metrics: List<Int>): Trend<Int> {
        val current = metrics.take(WEEKLY_HISTORY).sum()

        val previous = metrics.drop(WEEKLY_HISTORY).take(WEEKLY_HISTORY).sum()

        return when {
            previous == 0 -> Trend.Neutral(current)

            else -> {
                val change = (current - previous).toFloat() / previous

                when {
                    change > 0 -> Trend.Positive(current, change)

                    change < 0 -> Trend.Negative(current, change)

                    else -> Trend.Neutral(current)
                }
            }
        }
    }

    override suspend fun getProjects() = projectService.getProjects(FIRST_PAGE).mapCatching { repos ->
        repos.map { repo ->
            val (cloneTrafficRes, viewTrafficRes) = coroutineScope {
                async { projectService.getCloneTraffic(repo) }.await() to async { projectService.getViewTraffic(repo) }.await()
            }

            val cloneTraffic = cloneTrafficRes.getOrThrow()

            val viewTraffic = viewTrafficRes.getOrThrow()

            val clones = cloneTraffic.clones.map(GitHubDailyTraffic::count)

            val cloners = cloneTraffic.clones.map(GitHubDailyTraffic::uniques)

            val views = viewTraffic.views.map(GitHubDailyTraffic::count)

            val visitors = viewTraffic.views.map(GitHubDailyTraffic::uniques)

            with(repo) {
                Project(
                    id = id,
                    name = name,
                    fullName = fullName,
                    description = description,
                    url = url,
                    stargazers = stargazers,
                    forks = forks,
                    clonesTrend = calculateTrend(clones),
                    clonersTrend = calculateTrend(cloners),
                    viewsTrend = calculateTrend(views),
                    visitorsTrend = calculateTrend(visitors),
                    createdAt = Instant.parse(createdAt).toEpochMilli().milliseconds,
                    pushedAt = Instant.parse(pushedAt).toEpochMilli().milliseconds
                )
            }
        }
    }
}