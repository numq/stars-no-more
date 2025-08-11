package io.github.numq.starsnomore.dashboard

import io.github.numq.starsnomore.event.Event
import java.util.*

sealed class DashboardEvent private constructor() : Event<UUID> {
    override val key: UUID = UUID.randomUUID()

    data object RefreshProjects : DashboardEvent()
}