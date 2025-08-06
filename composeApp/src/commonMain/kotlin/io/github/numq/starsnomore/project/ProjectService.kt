package io.github.numq.starsnomore.project

interface ProjectService {
    suspend fun getProjects(page: Int): Result<List<GitHubProject>>

    suspend fun getCloneTraffic(githubProject: GitHubProject): Result<GitHubClones>

    suspend fun getViewTraffic(githubProject: GitHubProject): Result<GitHubViews>
}