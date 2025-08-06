package io.github.numq.starsnomore.project

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.numq.starsnomore.growth.Growth
import io.github.numq.starsnomore.growth.GrowthChart
import io.github.numq.starsnomore.growth.GrowthItem
import java.text.SimpleDateFormat
import java.util.*

private const val SHORT_DATE_PATTERN = "M/d/yyyy"

@Composable
private fun TableCell(modifier: Modifier, text: String) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun GrowthCell(modifier: Modifier, growth: Growth<Int>) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        GrowthItem(modifier = Modifier.fillMaxSize(), growth = growth)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CellTooltip(modifier: Modifier, tooltip: (@Composable () -> Unit)?, content: @Composable () -> Unit) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        TooltipArea(tooltip = {
            if (tooltip != null) {
                Card {
                    Box(modifier = Modifier.padding(4.dp), contentAlignment = Alignment.Center) {
                        tooltip()
                    }
                }
            }
        }) {
            content()
        }
    }
}

@Composable
fun ProjectRow(
    modifier: Modifier,
    project: Project,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val cellModifier = Modifier.weight(1f)

        val growthTooltipModifier = Modifier.size(256.dp, 128.dp).padding(4.dp)

        CellTooltip(modifier = cellModifier, tooltip = {
            Text(text = project.name)
        }, content = {
            TableCell(modifier = Modifier.fillMaxSize().padding(4.dp), text = project.name)
        })

        TableCell(modifier = cellModifier.padding(4.dp), text = "${project.stargazers}")

        TableCell(modifier = cellModifier.padding(4.dp), text = "${project.forks}")

        CellTooltip(modifier = cellModifier, tooltip = {
            val allValues = project.clonesGrowth.previousWeek + project.clonesGrowth.currentWeek

            val minValue = allValues.minOrNull() ?: 0

            val maxValue = allValues.maxOrNull() ?: 0

            if ((project.clonesGrowth.previousWeek.size > 1 || project.clonesGrowth.previousWeek.size > 1) && maxValue > 0) {
                GrowthChart(
                    modifier = growthTooltipModifier,
                    minValue = minValue,
                    maxValue = maxValue,
                    growth = project.clonesGrowth
                )
            }
        }, content = {
            GrowthCell(modifier = Modifier.fillMaxSize().padding(4.dp), growth = project.clonesGrowth)
        })

        CellTooltip(modifier = cellModifier, tooltip = {
            val allValues = project.clonersGrowth.previousWeek + project.clonersGrowth.currentWeek

            val minValue = allValues.minOrNull() ?: 0

            val maxValue = allValues.maxOrNull() ?: 0

            if ((project.clonersGrowth.previousWeek.size > 1 || project.clonersGrowth.previousWeek.size > 1) && maxValue > 0) {
                GrowthChart(
                    modifier = growthTooltipModifier,
                    minValue = minValue,
                    maxValue = maxValue,
                    growth = project.clonersGrowth
                )
            }
        }, content = {
            GrowthCell(modifier = Modifier.fillMaxSize().padding(4.dp), growth = project.clonersGrowth)
        })

        CellTooltip(modifier = cellModifier, tooltip = {
            val allValues = project.viewsGrowth.previousWeek + project.viewsGrowth.currentWeek

            val minValue = allValues.minOrNull() ?: 0

            val maxValue = allValues.maxOrNull() ?: 0

            if ((project.viewsGrowth.previousWeek.size > 1 || project.viewsGrowth.previousWeek.size > 1) && maxValue > 0) {
                GrowthChart(
                    modifier = growthTooltipModifier,
                    minValue = minValue,
                    maxValue = maxValue,
                    growth = project.viewsGrowth
                )
            }
        }, content = {
            GrowthCell(modifier = Modifier.fillMaxSize().padding(4.dp), growth = project.viewsGrowth)
        })

        CellTooltip(modifier = cellModifier, tooltip = {
            val allValues = project.visitorsGrowth.previousWeek + project.visitorsGrowth.currentWeek

            val minValue = allValues.minOrNull() ?: 0

            val maxValue = allValues.maxOrNull() ?: 0

            if ((project.visitorsGrowth.previousWeek.size > 1 || project.visitorsGrowth.previousWeek.size > 1) && maxValue > 0) {
                GrowthChart(
                    modifier = growthTooltipModifier,
                    minValue = minValue,
                    maxValue = maxValue,
                    growth = project.visitorsGrowth
                )
            }
        }, content = {
            GrowthCell(modifier = Modifier.fillMaxSize().padding(4.dp), growth = project.visitorsGrowth)
        })

        CellTooltip(modifier = cellModifier, tooltip = {
            Text(SimpleDateFormat.getDateTimeInstance().format(Date(project.createdAt.inWholeMilliseconds)))
        }, content = {
            TableCell(
                modifier = Modifier.fillMaxSize().padding(4.dp),
                text = SimpleDateFormat(SHORT_DATE_PATTERN).format(Date(project.createdAt.inWholeMilliseconds))
            )
        })

        CellTooltip(modifier = cellModifier, tooltip = {
            Text(SimpleDateFormat.getDateTimeInstance().format(Date(project.pushedAt.inWholeMilliseconds)))
        }, content = {
            TableCell(
                modifier = Modifier.fillMaxSize().padding(4.dp),
                text = SimpleDateFormat(SHORT_DATE_PATTERN).format(Date(project.pushedAt.inWholeMilliseconds))
            )
        })
    }
}