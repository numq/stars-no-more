package com.github.numq.repo

actual interface RepoService {
    actual suspend fun getRepos(page: Int): Result<List<GithubRepo>>
    actual suspend fun getClones(githubRepo: GithubRepo): Result<GithubClones>
    actual suspend fun getViews(githubRepo: GithubRepo): Result<GithubViews>
}