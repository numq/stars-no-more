package io.github.numq.starsnomore.credentials

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(val name: String = "", val token: String = "")