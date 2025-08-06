package io.github.numq.starsnomore.project

import io.github.numq.starsnomore.interactor.Interactor

class GetProjects(private val projectRepository: ProjectRepository) : Interactor<Unit, List<Project>> {
    override suspend fun execute(input: Unit) = projectRepository.getProjects()
}