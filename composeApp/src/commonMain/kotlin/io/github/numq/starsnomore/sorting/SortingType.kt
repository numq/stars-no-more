package io.github.numq.starsnomore.sorting

enum class SortingType(val displayName: String, val description: String?) {
    NAME(displayName = "Name", description = "Name of the repository owner"),
    STARGAZERS(displayName = "Stargazers", description = "All time quantity"),
    FORKS(displayName = "Forks", description = "All time quantity"),
    CLONES(displayName = "Clones", description = "Over the past two weeks"),
    CLONERS(displayName = "Cloners", description = "Over the past two weeks"),
    VIEWS(displayName = "Views", description = "Over the past two weeks"),
    VISITORS(displayName = "Visitors", description = "Over the past two weeks"),
    CREATED_AT(displayName = "Created At", description = null),
    PUSHED_AT(displayName = "Pushed At", description = null)
}