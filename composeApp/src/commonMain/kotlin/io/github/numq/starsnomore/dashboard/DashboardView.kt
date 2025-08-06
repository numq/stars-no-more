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
import io.github.numq.starsnomore.sorting.SortingCriteria
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardView(feature: DashboardFeature = koinInject()) {
    val coroutineScope = rememberCoroutineScope()

    val state by feature.state.collectAsState()

    val listState = rememberLazyListState()

    var isContextMenuVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.selectedSortingCriteria, state.isAscending) {
        listState.scrollToItem(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .onClick(matcher = PointerMatcher.mouse(PointerButton.Secondary), onClick = {
                isContextMenuVisible = true
            }), floatingActionButtonPosition = FabPosition.Center
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
                    TooltipArea(
                        tooltip = {
                            criteria.tooltip?.let { tooltip ->
                                Card {
                                    Box(modifier = Modifier.padding(4.dp), contentAlignment = Alignment.Center) {
                                        Text(text = tooltip)
                                    }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(IntrinsicSize.Max)
                            .clickable(enabled = !state.isLoadingProjects) {
                                coroutineScope.launch {
                                    feature.execute(DashboardCommand.SortProjects(criteria = criteria))
                                }
                            }.padding(vertical = 8.dp, horizontal = 4.dp)
                    ) {
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
                                        modifier = Modifier.size(16.dp).rotate(if (state.isAscending) 180f else 0f),
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
                    state.isLoadingProjects -> CircularProgressIndicator()

                    state.projects.isEmpty() -> Text("No repositories found")

                    else -> ContextMenuArea(items = {
                        listOf(
                            ContextMenuItem("Refresh data", onClick = {
                                coroutineScope.launch {
                                    feature.execute(DashboardCommand.UpdateLoadingState(isLoadingProjects = true))

                                    feature.execute(DashboardCommand.GetProjects)
                                }
                            })
                        )
                    }) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Top)
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
}