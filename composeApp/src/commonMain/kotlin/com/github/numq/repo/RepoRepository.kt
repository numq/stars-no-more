package com.github.numq.repo

interface RepoRepository {
    suspend fun getRepos(): Result<List<Repo>>

    companion object {
        fun create(service: RepoService): RepoRepository = DefaultRepoRepository(service)
    }
}