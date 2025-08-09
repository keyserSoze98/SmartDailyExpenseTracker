package com.keysersoze.smartdailyexpensetracker.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keysersoze.smartdailyexpensetracker.domain.model.Expense
import com.keysersoze.smartdailyexpensetracker.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class ExpenseEntryUiState(
    val title: String = "",
    val amountText: String = "",
    val category: String = "Food",
    val notes: String = "",
    val receiptUrl: String? = null,
    val todayTotal: Double = 0.0,
    val errorMessage: String? = null,
    val lastAddedId: Long? = null
)

@HiltViewModel
class ExpenseEntryViewModel @Inject constructor(
    private val repo: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseEntryUiState())
    val uiState: StateFlow<ExpenseEntryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getTodayExpenses()
                .map { list -> list.sumOf { it.amount } }
                .collect { total ->
                    _uiState.update { it.copy(todayTotal = total) }
                }
        }
    }

    fun onTitleChange(s: String) = _uiState.update { it.copy(title = s) }
    fun onAmountChange(s: String) = _uiState.update { it.copy(amountText = s) }
    fun onCategoryChange(c: String) = _uiState.update { it.copy(category = c) }
    fun onNotesChange(s: String) {
        val truncated = if (s.length > 100) s.take(100) else s
        _uiState.update { it.copy(notes = truncated) }
    }
    fun onReceiptUrlChange(url: String?) = _uiState.update { it.copy(receiptUrl = url) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addExpense(onSuccess: (() -> Unit)? = null) {
        val state = _uiState.value
        val title = state.title.trim()
        val amount = state.amountText.toDoubleOrNull() ?: -1.0

        when {
            title.isEmpty() -> {
                _uiState.update { it.copy(errorMessage = "Title is required") }
                return
            }
            amount <= 0.0 -> {
                _uiState.update { it.copy(errorMessage = "Amount must be > 0") }
                return
            }
            else -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }

        val expense = Expense(
            id = System.currentTimeMillis(),
            title = title,
            amount = amount,
            category = state.category,
            notes = state.notes.ifBlank { null },
            receiptUrl = state.receiptUrl,
            dateTime = LocalDateTime.now()
        )

        viewModelScope.launch {
            repo.addExpense(expense)
            _uiState.update {
                it.copy(
                    title = "",
                    amountText = "",
                    notes = "",
                    receiptUrl = null,
                    lastAddedId = expense.id,
                    errorMessage = null
                )
            }
            onSuccess?.invoke()
        }
    }

    fun clearLastAddedFlag() {
        _uiState.update { it.copy(lastAddedId = null) }
    }
}