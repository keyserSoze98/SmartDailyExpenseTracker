package com.keysersoze.smartdailyexpensetracker.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keysersoze.smartdailyexpensetracker.ui.viewmodels.ExpenseReportViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseReportScreen(
    viewModel: ExpenseReportViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Daily Totals (Last 7 Days)", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        ExpenseReportChart(
            data = state.dailyTotals.map { it.total },
            labels = state.dailyTotals.map { it.date }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Category Totals", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(state.categoryTotals) { category ->
                Text("${category.category}: ₹${category.total}")
            }
        }
    }
}

@Composable
fun ExpenseReportChart(
    data: List<Float>,
    labels: List<String>,
    barColor: Color = MaterialTheme.colorScheme.primary
) {
    val maxValue = (data.maxOrNull() ?: 0f).coerceAtLeast(1f)

    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Fixed row for amounts (avoids overlap forever)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                data.forEach { value ->
                    Text(
                        text = "₹${value.toInt()}",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }

            val chartHeight = 200.dp
            val labelGap = 16.dp

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight + labelGap + 20.dp)
            ) {
                val barSpace = size.width / data.size
                val barWidth = barSpace * 0.5f
                val usableHeight = size.height - labelGap.toPx() - 20.dp.toPx()

                // Grid lines
                val steps = 5
                repeat(steps + 1) { step ->
                    val y = usableHeight - (usableHeight / steps) * step
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                }

                // Bars + labels
                data.forEachIndexed { index, value ->
                    val barHeight = (value / maxValue) * usableHeight
                    val xCenter = barSpace * index + barSpace / 2f

                    // Draw bar
                    drawLine(
                        color = barColor,
                        start = Offset(x = xCenter, y = usableHeight),
                        end = Offset(x = xCenter, y = usableHeight - barHeight),
                        strokeWidth = barWidth,
                        cap = StrokeCap.Round
                    )

                    // X-axis label
                    drawContext.canvas.nativeCanvas.apply {
                        val paint = android.graphics.Paint().apply {
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 12.sp.toPx()
                            color = android.graphics.Color.BLACK
                        }
                        drawText(
                            labels[index],
                            xCenter,
                            usableHeight + labelGap.toPx() + paint.textSize,
                            paint
                        )
                    }
                }
            }
        }
    }
}