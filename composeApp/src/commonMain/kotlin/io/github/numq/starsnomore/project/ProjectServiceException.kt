package io.github.numq.starsnomore.project

data class ProjectServiceException(override val message: String, val status: Int, val body: String?) : Exception(message)