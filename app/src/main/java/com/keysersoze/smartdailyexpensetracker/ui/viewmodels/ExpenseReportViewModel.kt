package com.keysersoze.smartdailyexpensetracker.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
data class DailyTotal(val date: String, val total: Float)
data class CategoryTotal(val category: String, val total: Float)

@RequiresApi(Build.VERSION_CODES.O)
data class ExpenseReportUiState(
    val dailyTotals: List<DailyTotal> = emptyList(),
    val categoryTotals: List<CategoryTotal> = emptyList()
)

@RequiresApi(Build.VERSION_CODES.O)
class ExpenseReportViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseReportUiState())
    val uiState: StateFlow<ExpenseReportUiState> = _uiState

    init {
        loadMockData()
    }

    private fun loadMockData() {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("dd MMM")

            // Mock: last 7 days totals
            val daily = (0..6).map { daysAgo ->
                val date = LocalDate.now().minusDays((6 - daysAgo).toLong())
                DailyTotal(date.format(formatter), Random.nextInt(200, 1000).toFloat())
            }

            // Mock: category totals
            val categories = listOf("Food", "Transport", "Shopping", "Bills", "Other")
            val categoryTotals = categories.map {
                CategoryTotal(it, Random.nextInt(300, 1500).toFloat())
            }

            _uiState.value = ExpenseReportUiState(
                dailyTotals = daily,
                categoryTotals = categoryTotals
            )
        }
    }
}