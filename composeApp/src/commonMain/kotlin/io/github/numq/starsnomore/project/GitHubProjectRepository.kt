package io.github.numq.starsnomore.project

import io.github.numq.starsnomore.growth.Growth
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

internal class GitHubProjectRepository(private val projectService: ProjectService) : ProjectRepository {
    private companion object {
        const val WEEKLY_HISTORY = 7
    }

    private fun calculateGrowth(metrics: List<Int>): Growth<Int> {
        val previousWeek = metrics.drop(WEEKLY_HISTORY).take(WEEKLY_HISTORY)

        val currentWeek = metrics.take(WEEKLY_HISTORY)

        val previous = previousWeek.sum()

        val current = currentWeek.sum()

        return when {
            previous == 0 -> Growth.Neutral(value = current, previousWeek = previousWeek, currentWeek = currentWeek)

            else -> {
                val change = (current - previous).toFloat() / previous

                when {
                    change > 0 -> Growth.Positive(
                        value = current, percentage = change, previousWeek = previousWeek, currentWeek = currentWeek
                    )

                    change < 0 -> Growth.Negative(
                        value = current, percentage = change, previousWeek = previousWeek, currentWeek = currentWeek
                    )

                    else -> Growth.Neutral(value = current, previousWeek = previousWeek, currentWeek = currentWeek)
                }
            }
        }
    }

    override suspend fun getProjects() = projectService.getProjects().mapCatching { gitHubProjects ->
        gitHubProjects.map { gitHubProject ->
            val (cloneTrafficRes, viewTrafficRes) = coroutineScope {
                Pair(
                    async { projectService.getCloneTraffic(gitHubProject) }.await(),
                    async { projectService.getViewTraffic(gitHubProject) }.await()
                )
            }

            val cloneTraffic = cloneTrafficRes.getOrThrow()

            val viewTraffic = viewTrafficRes.getOrThrow()

            val clones = cloneTraffic.clones.map(GitHubDailyTraffic::count)

            val cloners = cloneTraffic.clones.map(GitHubDailyTraffic::uniques)

            val views = viewTraffic.views.map(GitHubDailyTraffic::count)

            val visitors = viewTraffic.views.map(GitHubDailyTraffic::uniques)

            with(gitHubProject) {
                Project(
                    id = id,
                    name = name,
                    fullName = fullName,
                    description = description,
                    url = url,
                    stargazers = stargazers,
                    forks = forks,
                    clonesGrowth = calculateGrowth(clones),
                    clonersGrowth = calculateGrowth(cloners),
                    viewsGrowth = calculateGrowth(views),
                    visitorsGrowth = calculateGrowth(visitors),
                    createdAt = Instant.parse(createdAt).toEpochMilli().milliseconds,
                    pushedAt = Instant.parse(pushedAt).toEpochMilli().milliseconds
                )
            }
        }
    }
}