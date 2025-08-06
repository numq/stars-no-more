package io.github.numq.starsnomore.trend

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import starsnomore.composeapp.generated.resources.Res
import starsnomore.composeapp.generated.resources.arrow_drop_down_24dp
import starsnomore.composeapp.generated.resources.arrow_drop_up_24dp
import starsnomore.composeapp.generated.resources.equal_24dp

private val NeutralColor = Color.Gray
private val NegativeColor = Color(0xFFF44336)
private val PositiveColor = Color(0xFF4CAF50)

@Composable
fun TrendItem(modifier: Modifier, trend: Trend<Int>) {
    val animatedValue by animateIntAsState(
        targetValue = trend.value, animationSpec = tween(500)
    )

    var isTrendVisible by remember { mutableStateOf(false) }

    LaunchedEffect(trend) {
        isTrendVisible = false

        delay(16)

        isTrendVisible = true
    }

    Card {
        Column(
            modifier = modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically)
        ) {
            Text("$animatedValue", fontSize = 28.sp, fontWeight = FontWeight.Bold)

            HorizontalDivider()

            AnimatedVisibility(
                visible = isTrendVisible,
                enter = fadeIn(tween(500)),
                exit = fadeOut(tween(500))
            ) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    when (trend) {
                        is Trend.Neutral ->
                            Icon(
                                painterResource(Res.drawable.equal_24dp),
                                null,
                                modifier = Modifier.size(18.dp),
                                tint = NeutralColor
                            )

                        is Trend.Negative -> {
                            Text(
                                "%.1f%%".format(trend.percentage),
                                fontSize = 14.sp,
                                color = NegativeColor,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            Icon(
                                painterResource(Res.drawable.arrow_drop_down_24dp),
                                null,
                                modifier = Modifier.size(18.dp),
                                tint = NegativeColor
                            )
                        }

                        is Trend.Positive -> {
                            Text(
                                "%.1f%%".format(trend.percentage),
                                fontSize = 14.sp,
                                color = PositiveColor,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            Icon(
                                painterResource(Res.drawable.arrow_drop_up_24dp),
                                null,
                                modifier = Modifier.size(18.dp),
                                tint = PositiveColor
                            )
                        }
                    }
                }
            }
        }
    }
}