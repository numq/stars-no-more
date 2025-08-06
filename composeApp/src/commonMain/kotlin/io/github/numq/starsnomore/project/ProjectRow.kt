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
import io.github.numq.starsnomore.trend.Trend
import io.github.numq.starsnomore.trend.TrendItem
import java.text.SimpleDateFormat
import java.util.*

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
private fun TrendCell(modifier: Modifier, trend: Trend<Int>) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        TrendItem(modifier = Modifier.fillMaxSize(), trend = trend)
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

        CellTooltip(modifier = cellModifier, tooltip = {
            Text(text = project.name)
        }, content = {
            TableCell(modifier = Modifier.fillMaxSize().padding(4.dp), text = project.name)
        })

        TableCell(modifier = cellModifier.padding(4.dp), text = "${project.stargazers}")

        TableCell(modifier = cellModifier.padding(4.dp), text = "${project.forks}")

        TrendCell(modifier = cellModifier.padding(4.dp), trend = project.clonesTrend)

        TrendCell(modifier = cellModifier.padding(4.dp), trend = project.clonersTrend)

        TrendCell(modifier = cellModifier.padding(4.dp), trend = project.viewsTrend)

        TrendCell(modifier = cellModifier.padding(4.dp), trend = project.visitorsTrend)

        CellTooltip(modifier = cellModifier, tooltip = {
            Text(SimpleDateFormat.getDateTimeInstance().format(Date(project.createdAt.inWholeMilliseconds)))
        }, content = {
            TableCell(
                modifier = Modifier.fillMaxSize().padding(4.dp),
                text = SimpleDateFormat("MMM dd").format(Date(project.createdAt.inWholeMilliseconds))
            )
        })

        CellTooltip(modifier = cellModifier, tooltip = {
            Text(SimpleDateFormat.getDateTimeInstance().format(Date(project.pushedAt.inWholeMilliseconds)))
        }, content = {
            TableCell(
                modifier = Modifier.fillMaxSize().padding(4.dp),
                text = SimpleDateFormat("MMM dd").format(Date(project.pushedAt.inWholeMilliseconds))
            )
        })
    }
}