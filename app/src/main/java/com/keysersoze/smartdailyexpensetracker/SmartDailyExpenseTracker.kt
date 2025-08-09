package com.keysersoze.smartdailyexpensetracker

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmartDailyExpenseTracker : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("@@@Application", "Created!")
    }
}