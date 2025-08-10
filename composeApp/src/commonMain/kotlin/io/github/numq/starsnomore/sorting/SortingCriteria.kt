package io.github.numq.starsnomore.sorting

sealed class SortingCriteria private constructor(open val type: SortingType, open val direction: SortingDirection) {
    data class Name(override val direction: SortingDirection) : SortingCriteria(
        type = SortingType.NAME,
        direction = direction
    )

    data class Stargazers(override val direction: SortingDirection) : SortingCriteria(
        type = SortingType.STARGAZERS,
        direction = direction
    )

    data class Forks(override val direction: SortingDirection) : SortingCriteria(
        type = SortingType.FORKS,
        direction = direction
    )

    sealed class Traffic private constructor(
        override val type: SortingType,
        override val direction: SortingDirection,
    ) : SortingCriteria(type = type, direction = direction) {
        data class Clones(override val direction: SortingDirection) : Traffic(
            type = SortingType.CLONES,
            direction = direction
        )

        data class Cloners(override val direction: SortingDirection) : Traffic(
            type = SortingType.CLONERS,
            direction = direction
        )

        data class Views(override val direction: SortingDirection) : Traffic(
            type = SortingType.VIEWS,
            direction = direction
        )

        data class Visitors(override val direction: SortingDirection) : Traffic(
            type = SortingType.VISITORS,
            direction = direction
        )
    }

    sealed class Date private constructor(
        override val type: SortingType,
        override val direction: SortingDirection,
    ) : SortingCriteria(type = type, direction = direction) {
        data class CreatedAt(override val direction: SortingDirection) : Date(
            type = SortingType.CREATED_AT,
            direction = direction
        )

        data class PushedAt(override val direction: SortingDirection) : Date(
            type = SortingType.PUSHED_AT,
            direction = direction
        )
    }
}