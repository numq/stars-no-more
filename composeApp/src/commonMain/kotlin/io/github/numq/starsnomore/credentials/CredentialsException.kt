package io.github.numq.starsnomore.credentials

data class CredentialsException(override val message: String) : Exception(message)