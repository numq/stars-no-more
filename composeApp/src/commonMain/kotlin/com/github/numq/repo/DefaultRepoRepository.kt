package com.github.numq.repo

import java.time.Instant

class DefaultRepoRepository(private val repoService: RepoService) : RepoRepository {
    private companion object {
        const val FIRST_PAGE = 1
    }

    override suspend fun getRepos() = repoService.getRepos(FIRST_PAGE).mapCatching { repos ->
        repos.map { repo ->
            val clones = repoService.getClones(repo).getOrThrow()
            val views = repoService.getViews(repo).getOrThrow()
            with(repo) {
                Repo(
                    id = id,
                    name = name,
                    fullName = fullName,
                    description = description,
                    url = url,
                    stargazers = stargazers,
                    forks = forks,
                    isPrivate = isPrivate,
                    clones = clones.count,
                    uniqueCloners = clones.uniques,
                    views = views.count,
                    uniqueVisitors = views.uniques,
                    createdAt = Instant.parse(createdAt).toEpochMilli(),
                    pushedAt = Instant.parse(pushedAt).toEpochMilli()
                )
            }
        }
    }
}