package com.github.numq.repo

expect interface RepoService {
    suspend fun getRepos(page: Int): Result<List<GithubRepo>>
    suspend fun getClones(githubRepo: GithubRepo): Result<GithubClones>
    suspend fun getViews(githubRepo: GithubRepo): Result<GithubViews>
}