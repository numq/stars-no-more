package io.github.numq.starsnomore.growth

sealed interface Growth<T : Number> {
    val value: T

    val percentage: Float

    val previousWeek: List<T>

    val currentWeek: List<T>

    data class Neutral<T : Number>(
        override val value: T,
        override val previousWeek: List<T>,
        override val currentWeek: List<T>,
    ) : Growth<T> {
        override val percentage = 0f
    }

    data class Negative<T : Number>(
        override val value: T,
        override val percentage: Float,
        override val previousWeek: List<T>,
        override val currentWeek: List<T>,
    ) : Growth<T>

    data class Positive<T : Number>(
        override val value: T,
        override val percentage: Float,
        override val previousWeek: List<T>,
        override val currentWeek: List<T>,
    ) : Growth<T>
}