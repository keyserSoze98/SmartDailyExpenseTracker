package com.keysersoze.smartdailyexpensetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String? = null,
    val receiptUrl: String? = null,
    val createdAt: Long // epoch millis
)