package com.github.numq.repo

import com.github.numq.usecase.UseCase

class GetRepos(private val repoRepository: RepoRepository) : UseCase<Unit, List<Repo>> {
    override suspend fun execute(input: Unit) = repoRepository.getRepos()
}