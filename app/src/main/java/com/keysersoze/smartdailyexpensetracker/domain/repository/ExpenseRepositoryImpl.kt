package com.keysersoze.smartdailyexpensetracker.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.keysersoze.smartdailyexpensetracker.data.local.ExpenseDao
import com.keysersoze.smartdailyexpensetracker.data.local.toDomain
import com.keysersoze.smartdailyexpensetracker.data.local.toEntity
import com.keysersoze.smartdailyexpensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao
) : ExpenseRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getExpensesForDate(date: LocalDate): Flow<List<Expense>> {
        val startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endOfDay = date.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        return dao.getExpensesForDayFlow(startOfDay, endOfDay)
            .map { list -> list.map { it.toDomain() } }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTodayExpenses(): Flow<List<Expense>> {
        return getExpensesForDate(LocalDate.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addExpense(expense: Expense) {
        dao.insertExpense(expense.toEntity())
    }
}