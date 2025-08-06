package io.github.numq.starsnomore.growth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GrowthChart(modifier: Modifier, minValue: Int, maxValue: Int, growth: Growth<Int>) {
    require(maxValue > 0) { "Unable to plot a chart with zero maximum extreme" }

    val previousWeekColor = Color.LightGray

    val currentWeekColor = MaterialTheme.colorScheme.primary

    val dotRadius = 3.dp

    val lineStrokeWidth = 2.dp

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeW = lineStrokeWidth.toPx()

            val dotR = dotRadius.toPx()

            val padding = dotR + strokeW

            val availableWidth = size.width - 2 * padding

            val availableHeight = size.height - 2 * padding

            fun calculateY(value: Int): Float {
                val normalized = (value - minValue).toFloat() / (maxValue - minValue).toFloat()

                return padding + availableHeight * (1f - normalized.coerceIn(0f, 1f))
            }

            fun calculateX(index: Int, size: Int): Float {
                val stepX = if (size > 1) availableWidth / (size - 1) else 0f

                return padding + stepX * index
            }

            fun drawGrowthLine(data: List<Int>, color: Color, bold: Boolean = false) {
                if (data.size < 2) return

                data.zipWithNext().forEachIndexed { i, (start, end) ->
                    drawLine(
                        color = color,
                        start = Offset(calculateX(i, data.size), calculateY(start)),
                        end = Offset(calculateX(i + 1, data.size), calculateY(end)),
                        strokeWidth = if (bold) strokeW * 1.5f else strokeW
                    )
                }

                data.forEachIndexed { i, value ->
                    drawCircle(
                        color = color,
                        radius = dotR,
                        center = Offset(calculateX(i, data.size), calculateY(value))
                    )
                }
            }

            drawGrowthLine(growth.previousWeek, previousWeekColor)

            drawGrowthLine(growth.currentWeek, currentWeekColor, bold = true)
        }
    }
}