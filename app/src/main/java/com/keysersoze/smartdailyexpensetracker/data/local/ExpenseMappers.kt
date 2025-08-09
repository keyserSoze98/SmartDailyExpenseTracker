package com.keysersoze.smartdailyexpensetracker.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import com.keysersoze.smartdailyexpensetracker.domain.model.Expense
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun ExpenseEntity.toDomain(): Expense =
    Expense(
        id = id,
        title = title,
        amount = amount,
        category = category,
        notes = notes,
        receiptUrl = receiptUrl,
        dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault())
    )

@RequiresApi(Build.VERSION_CODES.O)
fun Expense.toEntity(): ExpenseEntity =
    ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        category = category,
        notes = notes,
        receiptUrl = receiptUrl,
        createdAt = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
