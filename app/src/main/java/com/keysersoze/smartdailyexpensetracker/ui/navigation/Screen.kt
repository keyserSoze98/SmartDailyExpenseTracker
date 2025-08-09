package com.keysersoze.smartdailyexpensetracker.ui.navigation

import androidx.annotation.DrawableRes
import com.keysersoze.smartdailyexpensetracker.R

sealed class Screen(val route: String, val title: String, @DrawableRes val icon: Int) {
    object ExpenseEntry : Screen("expense_entry", "Entry", R.drawable.ic_entry)
    object ExpenseList : Screen("expense_list", "List", R.drawable.ic_list)
    object ExpenseReport : Screen("expense_report", "Report", R.drawable.ic_report)
}

val bottomNavItems = listOf(
    Screen.ExpenseEntry,
    Screen.ExpenseList,
    Screen.ExpenseReport
)