package com.github.numq

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.numq.repo.GetRepos
import com.github.numq.repo.Repo
import com.github.numq.sorting.SortingCriteria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App(getRepos: GetRepos) {
    val coroutineScope = rememberCoroutineScope { Dispatchers.Default }

    val listState = rememberLazyListState()

    val repos = remember { mutableStateListOf<Repo>() }

    var reposJob by remember { mutableStateOf<Job?>(null) }

    val buttonHeights = remember { mutableStateMapOf<SortingCriteria, Float>() }

    val buttonWidths = remember { mutableStateMapOf<SortingCriteria, Float>() }

    var selectedSortingCriteria by remember { mutableStateOf(SortingCriteria.STARGAZERS) }

    var isAscending by remember { mutableStateOf(false) }

    fun getSortingFunction(criteria: SortingCriteria, isAscending: Boolean): (Repo) -> Comparable<*> = when (criteria) {
        SortingCriteria.NAME -> if (isAscending) Repo::name else { it: Repo -> it.name.reversed() }

        SortingCriteria.STARGAZERS -> if (isAscending) Repo::stargazers else { it: Repo -> -it.stargazers }

        SortingCriteria.FORKS -> if (isAscending) Repo::forks else { it: Repo -> -it.forks }

        SortingCriteria.CLONES -> if (isAscending) Repo::clones else { it: Repo -> -it.clones }

        SortingCriteria.UNIQUE_CLONERS -> if (isAscending) Repo::uniqueCloners else { it: Repo -> -it.uniqueCloners }

        SortingCriteria.VIEWS -> if (isAscending) Repo::views else { it: Repo -> -it.views }

        SortingCriteria.UNIQUE_VISITORS -> if (isAscending) Repo::uniqueVisitors else { it: Repo -> -it.uniqueVisitors }

        SortingCriteria.CREATED_AT -> if (isAscending) Repo::createdAt else { it: Repo -> -it.createdAt }

        SortingCriteria.PUSHED_AT -> if (isAscending) Repo::pushedAt else { it: Repo -> -it.pushedAt }
    }

    fun refreshRepos() {
        reposJob?.cancel()
        reposJob = coroutineScope.launch {
            getRepos.execute(Unit).onSuccess {
                repos.clear()
                repos.addAll(
                    it.sortedWith(
                        compareBy(
                            getSortingFunction(selectedSortingCriteria, isAscending)
                        )
                    )
                )
            }.onFailure {
                println(it.localizedMessage)
            }
            reposJob = null
        }
    }

    LaunchedEffect(selectedSortingCriteria, isAscending) {
        val sortedRepos = repos.sortedWith(
            compareBy(
                getSortingFunction(selectedSortingCriteria, isAscending)
            )
        )

        repos.clear()

        repos.addAll(sortedRepos)

        listState.scrollToItem(0)
    }

    LaunchedEffect(Unit) {
        refreshRepos()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(), floatingActionButton = {
            AnimatedVisibility(visible = repos.isNotEmpty(),
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()) {
                FloatingActionButton(onClick = {
                    refreshRepos()
                }) {
                    Icon(Icons.Default.Refresh, null)
                }
            }
        }, floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SortingCriteria.entries.forEach { sortingCriteria ->
                    TextButton(modifier = Modifier.weight(1f).onSizeChanged {
                        buttonHeights[sortingCriteria] = it.height.toFloat()
                        buttonWidths[sortingCriteria] = it.width.toFloat()
                    }.height(buttonHeights.values.maxOrNull()?.dp ?: Dp.Unspecified)
                        .alpha(if (sortingCriteria == selectedSortingCriteria) .5f else 1f), onClick = {
                        if (sortingCriteria == selectedSortingCriteria) {
                            isAscending = !isAscending
                        } else {
                            selectedSortingCriteria = sortingCriteria
                        }
                    }) {
                        Text(
                            when (sortingCriteria) {
                                SortingCriteria.NAME -> "Name"

                                SortingCriteria.STARGAZERS -> "Stargazers"

                                SortingCriteria.FORKS -> "Forks"

                                SortingCriteria.CLONES -> "Clones"

                                SortingCriteria.UNIQUE_CLONERS -> "Unique\nCloners"

                                SortingCriteria.VIEWS -> "Views"

                                SortingCriteria.UNIQUE_VISITORS -> "Unique\nVisitors"

                                SortingCriteria.CREATED_AT -> "Created\nAt"

                                SortingCriteria.PUSHED_AT -> "Pushed\nAt"
                            }, modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Top),
                state = listState
            ) {
                items(repos, key = Repo::id) { repo ->
                    TooltipArea(tooltip = {
                        Text(repo.description)
                    }) {
                        Card {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(repo.name, modifier = Modifier.composed {
                                    buttonWidths[SortingCriteria.NAME]?.dp?.let { w ->
                                        width(w)
                                    } ?: weight(1f)
                                }, textAlign = TextAlign.Center)
                                Text("${repo.stargazers}", modifier = Modifier.composed {
                                    buttonWidths[SortingCriteria.STARGAZERS]?.dp?.let { w ->
                                        width(w)
                                    } ?: weight(1f)
                                }, textAlign = TextAlign.Center)
                                Text("${repo.forks}", modifier = Modifier.composed {
                                    buttonWidths[SortingCriteria.FORKS]?.dp?.let { w ->
                                        width(w)
                                    } ?: weight(1f)
                                }, textAlign = TextAlign.Center)
                                Text("${repo.clones}", modifier = Modifier.composed {
                                    buttonWidths[SortingCriteria.CLONES]?.dp?.let { w ->
                                        width(w)
                                    } ?: weight(1f)
                                }, textAlign = TextAlign.Center)
                                Text("${repo.uniqueCloners}", modifier = Modifier.composed {
                                    buttonWidths[SortingCriteria.UNIQUE_CLONERS]?.dp?.let { w ->
                                        width(w)
                                    } ?: weight(1f)
                                }, textAlign = TextAlign.Center)
                                Text("${repo.views}", modifier = Modifier.composed {
                                    buttonWidths[SortingCriteria.VIEWS]?.dp?.let { w ->
                                        width(w)
                                    } ?: weight(1f)
                                }, textAlign = TextAlign.Center)
                                Text("${repo.uniqueVisitors}", modifier = Modifier.composed {
                                    buttonWidths[SortingCriteria.UNIQUE_VISITORS]?.dp?.let { w ->
                                        width(w)
                                    } ?: weight(1f)
                                }, textAlign = TextAlign.Center)
                                Text(
                                    SimpleDateFormat.getDateInstance().format(Date(repo.createdAt)),
                                    modifier = Modifier.composed {
                                        buttonWidths[SortingCriteria.CREATED_AT]?.dp?.let { w ->
                                            width(w)
                                        } ?: weight(1f)
                                    },
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    SimpleDateFormat.getDateInstance().format(Date(repo.pushedAt)),
                                    modifier = Modifier.composed {
                                        buttonWidths[SortingCriteria.PUSHED_AT]?.dp?.let { w ->
                                            width(w)
                                        } ?: weight(1f)
                                    },
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}