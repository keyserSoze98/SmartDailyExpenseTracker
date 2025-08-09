package com.keysersoze.smartdailyexpensetracker.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class Expense(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String? = null,
    val receiptUrl: String? = null,
    val dateTime: LocalDateTime = LocalDateTime.now()
)