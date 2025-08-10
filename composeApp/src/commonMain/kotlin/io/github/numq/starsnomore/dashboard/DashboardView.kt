package io.github.numq.starsnomore.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.numq.starsnomore.project.Project
import io.github.numq.starsnomore.project.ProjectRow
import io.github.numq.starsnomore.project.ProjectServiceException
import io.github.numq.starsnomore.sorting.SortingDirection
import io.github.numq.starsnomore.sorting.SortingType
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardView(feature: DashboardFeature = koinInject()) {
    val coroutineScope = rememberCoroutineScope()

    val currentState by feature.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize().onClick(
            matcher = PointerMatcher.mouse(PointerButton.Secondary), onClick = {
                coroutineScope.launch {
                    feature.execute(DashboardCommand.OpenContextMenu)
                }
            })
    ) { paddingValues ->
        ContextMenuArea(items = {
            listOf(
                ContextMenuItem("Refresh", onClick = {
                    coroutineScope.launch {
                        feature.execute(DashboardCommand.RefreshProjects)
                    }
                })
            )
        }) {
            when (val state = currentState) {
                is DashboardState.Loading -> Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is DashboardState.Interactable.Loaded -> when {
                    state.projects.isEmpty() -> Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center
                    ) {
                        Text("The user does not own any repository")
                    }

                    else -> {
                        val listState = rememberLazyListState()

                        LaunchedEffect(state.selectedSortingCriteria) {
                            listState.animateScrollToItem(0)
                        }

                        Column(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(
                                space = 8.dp, alignment = Alignment.CenterVertically
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SortingType.entries.forEach { type ->
                                    TooltipArea(
                                        tooltip = {
                                        type.description?.let { tooltip ->
                                            Card {
                                                Box(
                                                    modifier = Modifier.padding(4.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = tooltip)
                                                }
                                            }
                                        }
                                    }, modifier = Modifier.weight(1f).height(IntrinsicSize.Max).clickable {
                                        coroutineScope.launch {
                                            feature.execute(DashboardCommand.SortProjects(type = type))
                                        }
                                    }.padding(vertical = 8.dp, horizontal = 4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = type.displayName,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.padding(bottom = 2.dp),
                                                    style = if (type == state.selectedSortingCriteria.type) {
                                                        MaterialTheme.typography.labelLarge.copy(
                                                            color = MaterialTheme.colorScheme.primary
                                                        )
                                                    } else {
                                                        MaterialTheme.typography.labelLarge
                                                    }
                                                )

                                                if (type == state.selectedSortingCriteria.type) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Filled.Sort,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(16.dp)
                                                            .rotate(if (state.selectedSortingCriteria.direction == SortingDirection.ASCENDING) 180f else 0f),
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                } else {
                                                    Spacer(modifier = Modifier.size(16.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            when {
                                state.projects.isEmpty() -> Box(
                                    modifier = Modifier.weight(1f), contentAlignment = Alignment.Center
                                ) {
                                    Text("The user does not own any repository")
                                }

                                else -> LazyColumn(
                                    modifier = Modifier.weight(1f),
                                    state = listState,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(
                                        space = 8.dp, alignment = Alignment.Top
                                    )
                                ) {
                                    items(state.sortedProjects, key = Project::id) { project ->
                                        ProjectRow(modifier = Modifier.fillMaxWidth(), project = project)
                                    }
                                }
                            }
                        }
                    }
                }

                is DashboardState.Interactable.Error -> Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(.5f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            space = 8.dp, alignment = Alignment.CenterVertically
                        )
                    ) {
                        Text("An error occurred", style = MaterialTheme.typography.labelLarge)

                        Text(state.exception.message ?: "Unknown error", style = MaterialTheme.typography.bodyMedium)

                        if (state.exception is ProjectServiceException) {
                            Text("Status: ${state.exception.status}", style = MaterialTheme.typography.bodySmall)

                            if (state.exception.body != null) {
                                Text(state.exception.body, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}