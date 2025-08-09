package com.keysersoze.smartdailyexpensetracker.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.keysersoze.smartdailyexpensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor() : ExpenseRepository {

    private val expensesFlow = MutableStateFlow<List<Expense>>(emptyList())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getExpensesForDate(date: LocalDate): Flow<List<Expense>> {
        return expensesFlow.map { list ->
            list.filter { it.dateTime.toLocalDate() == date }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTodayExpenses(): Flow<List<Expense>> {
        return getExpensesForDate(LocalDate.now())
    }

    override suspend fun addExpense(expense: Expense) {
        expensesFlow.value = expensesFlow.value + expense
    }
}