package com.keysersoze.smartdailyexpensetracker.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY createdAt DESC")
    fun getAllExpensesFlow(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE createdAt BETWEEN :start AND :end ORDER BY createdAt DESC")
    fun getExpensesBetweenFlow(start: Long, end: Long): Flow<List<ExpenseEntity>>

    // convenience for a date (caller computes day's start and end millis)
    @Query("SELECT * FROM expenses WHERE createdAt BETWEEN :dayStart AND :dayEnd ORDER BY createdAt DESC")
    fun getExpensesForDayFlow(dayStart: Long, dayEnd: Long): Flow<List<ExpenseEntity>>
}