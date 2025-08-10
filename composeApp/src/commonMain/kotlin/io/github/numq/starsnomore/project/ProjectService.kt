package io.github.numq.starsnomore.project

interface ProjectService {
    suspend fun getProjects(): Result<List<GitHubProject>>

    suspend fun getCloneTraffic(githubProject: GitHubProject): Result<GitHubClones>

    suspend fun getViewTraffic(githubProject: GitHubProject): Result<GitHubViews>
}