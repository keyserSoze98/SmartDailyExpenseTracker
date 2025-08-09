package com.keysersoze.smartdailyexpensetracker.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keysersoze.smartdailyexpensetracker.domain.model.Expense
import com.keysersoze.smartdailyexpensetracker.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
data class ExpenseListUiState(
    val expenses: List<Expense> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val groupByCategory: Boolean = false,
    val totalCount: Int = 0,
    val totalAmount: Double = 0.0
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val repo: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseListUiState())
    val uiState: StateFlow<ExpenseListUiState> = _uiState

    init {
        loadExpensesForDate(LocalDate.now())
    }

    fun loadExpensesForDate(date: LocalDate) {
        viewModelScope.launch {
            repo.getExpensesForDate(date).collect { list ->
                _uiState.update {
                    it.copy(
                        expenses = list,
                        selectedDate = date,
                        totalCount = list.size,
                        totalAmount = list.sumOf { e -> e.amount }
                    )
                }
            }
        }
    }

    fun toggleGroupMode() {
        _uiState.update { it.copy(groupByCategory = !it.groupByCategory) }
    }
}