package io.github.numq.starsnomore.project

interface ProjectRepository {
    suspend fun getProjects(): Result<List<Project>>
}