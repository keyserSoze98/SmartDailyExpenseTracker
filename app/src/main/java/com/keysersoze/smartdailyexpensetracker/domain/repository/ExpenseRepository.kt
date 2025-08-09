package com.keysersoze.smartdailyexpensetracker.domain.repository

import com.keysersoze.smartdailyexpensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExpenseRepository {
    fun getExpensesForDate(date: LocalDate): Flow<List<Expense>>
    fun getTodayExpenses(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
}