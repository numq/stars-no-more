package io.github.numq.starsnomore.trend

sealed interface Trend<T : Number> {
    val value: T

    val percentage: Float

    data class Neutral<T : Number>(override val value: T) : Trend<T> {
        override val percentage = 0f
    }

    data class Negative<T : Number>(override val value: T, override val percentage: Float) : Trend<T>

    data class Positive<T : Number>(override val value: T, override val percentage: Float) : Trend<T>
}