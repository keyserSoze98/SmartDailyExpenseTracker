package com.keysersoze.smartdailyexpensetracker

import AppNavHost
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.keysersoze.smartdailyexpensetracker.ui.theme.SmartDailyExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartDailyExpenseTrackerTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
