package io.github.numq.starsnomore.sorting

sealed interface SortingCriteria {
    val name: String

    val tooltip: String?

    data object Name : SortingCriteria {
        override val name = "Name"

        override val tooltip = null
    }

    data object Stargazers : SortingCriteria {
        override val name = "Stargazers"

        override val tooltip = "For all time"
    }

    data object Forks : SortingCriteria {
        override val name = "Forks"

        override val tooltip = "For all time"
    }

    sealed interface Traffic : SortingCriteria {
        data object Clones : Traffic {
            override val name = "Clones"

            override val tooltip = "Over the past two weeks"
        }

        data object Cloners : Traffic {
            override val name = "Cloners"

            override val tooltip = "Over the past two weeks"
        }

        data object Views : Traffic {
            override val name = "Views"

            override val tooltip = "Over the past two weeks"
        }

        data object Visitors : Traffic {
            override val name = "Visitors"

            override val tooltip = "Over the past two weeks"
        }
    }

    sealed interface Date : SortingCriteria {
        data object CreatedAt : Date {
            override val name = "Created at"

            override val tooltip = null
        }

        data object PushedAt : Date {
            override val name = "Pushed at"

            override val tooltip = null
        }
    }

    companion object {
        val values = listOf(
            Name,
            Stargazers,
            Forks,
            Traffic.Clones,
            Traffic.Cloners,
            Traffic.Views,
            Traffic.Visitors,
            Date.CreatedAt,
            Date.PushedAt
        )
    }
}