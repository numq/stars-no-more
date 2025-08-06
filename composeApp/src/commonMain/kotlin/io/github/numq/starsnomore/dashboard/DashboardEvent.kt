package io.github.numq.starsnomore.dashboard

import java.util.*

sealed class DashboardEvent private constructor() : io.github.numq.starsnomore.event.Event<UUID> {
    override val key: UUID = UUID.randomUUID()

    data class Error(val exception: Exception) : DashboardEvent()
}