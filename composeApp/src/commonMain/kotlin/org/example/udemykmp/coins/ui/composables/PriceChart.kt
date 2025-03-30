package org.example.udemykmp.coins.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.example.udemykmp.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PriceChart(
    modifier: Modifier = Modifier,
    pricePoints: List<Double>,
    profitColor: Color,
    lossColor: Color,
) {
    if (pricePoints.isEmpty()) return
    val maxPrice = pricePoints.maxOrNull() ?: return
    val minPrice = pricePoints.minOrNull() ?: return
    val minMaxDiff = maxPrice - minPrice
    val lineColor = if (pricePoints.last() > pricePoints.first()) profitColor else lossColor

    Canvas(modifier = modifier.fillMaxSize()) {
        val path = Path()

        pricePoints.forEachIndexed { index, pricePoint ->
            val x = index * (size.width/pricePoints.size)
            val y = size.height * ((pricePoint-minPrice)/minMaxDiff).toFloat()

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

//region PREVIEW
@Preview
@Composable
fun PriceChartPreview() {
    val pricePoints = listOf(10.0, 20.0, 15.0, 25.0, 30.0, 20.0, 15.0)
    AppTheme {
        PriceChart(
            pricePoints = pricePoints,
            profitColor = Color.Green,
            lossColor = Color.Red
        )
    }
}
//endregion