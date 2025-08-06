package io.github.numq.starsnomore.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.numq.starsnomore.project.Project
import io.github.numq.starsnomore.project.ProjectRow
import io.github.numq.starsnomore.sorting.SortingCriteria
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardView(feature: DashboardFeature = koinInject()) {
    val coroutineScope = rememberCoroutineScope()

    val state by feature.state.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(state.selectedSortingCriteria, state.isAscending) {
        listState.scrollToItem(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(), floatingActionButton = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    feature.execute(DashboardCommand.GetProjects)
                }
            }, modifier = Modifier.clip(CircleShape)) {
                Icon(Icons.Default.Refresh, null)
            }
        }, floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SortingCriteria.values.forEach { criteria ->
                    TooltipArea(tooltip = {
                        criteria.tooltip?.let { tooltip ->
                            Card {
                                Box(modifier = Modifier.padding(4.dp), contentAlignment = Alignment.Center) {
                                    Text(text = tooltip)
                                }
                            }
                        }
                    }, modifier = Modifier.weight(1f).height(IntrinsicSize.Max).clickable {
                        coroutineScope.launch {
                            feature.execute(DashboardCommand.SortProjects(criteria = criteria))
                        }
                    }.padding(vertical = 8.dp, horizontal = 4.dp)) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = criteria.name,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 2.dp),
                                    style = if (criteria == state.selectedSortingCriteria) {
                                        MaterialTheme.typography.labelLarge.copy(
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        MaterialTheme.typography.labelLarge
                                    }
                                )

                                if (criteria == state.selectedSortingCriteria) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Sort,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp).rotate(if (state.isAscending) 0f else 180f),
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
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                when {
                    state.projects.isEmpty() -> CircularProgressIndicator()

                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically)
                    ) {
                        items(state.projects, key = Project::id) { project ->
                            ProjectRow(modifier = Modifier.fillMaxWidth(), project = project)
                        }
                    }
                }
            }
        }
    }
}