package io.github.numq.starsnomore.growth

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import starsnomore.composeapp.generated.resources.Res
import starsnomore.composeapp.generated.resources.arrow_drop_down_24dp
import starsnomore.composeapp.generated.resources.arrow_drop_up_24dp
import starsnomore.composeapp.generated.resources.equal_24dp

private val NeutralColor = Color.Gray
private val NegativeColor = Color(0xFFF44336)
private val PositiveColor = Color(0xFF4CAF50)

@Composable
fun GrowthItem(modifier: Modifier, growth: Growth<Int>) {
    val animatedValue by animateIntAsState(
        targetValue = growth.value, animationSpec = tween(500)
    )

    Card {
        Column(
            modifier = modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically)
        ) {
            Text("$animatedValue", fontSize = 28.sp, fontWeight = FontWeight.Bold)

            HorizontalDivider()

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                when (growth) {
                    is Growth.Neutral ->
                        Icon(
                            painterResource(Res.drawable.equal_24dp),
                            null,
                            modifier = Modifier.size(18.dp),
                            tint = NeutralColor
                        )

                    is Growth.Negative -> {
                        Text(
                            "%.1f%%".format(growth.percentage),
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

                    is Growth.Positive -> {
                        Text(
                            "%.1f%%".format(growth.percentage),
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