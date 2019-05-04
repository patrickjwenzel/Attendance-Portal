package com.example.myapplication

import android.app.Application
import android.util.Log

class App : Application(), LifecycleDelegate {

    override fun onCreate() {
        super.onCreate()
        val lifeCycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifeCycleHandler)
    }

    override fun onAppBackgrounded() {
        Log.d("Awww", "App in background")
    }

    override fun onAppForegrounded() {
        Log.d("Yeeey", "App in foreground")
    }

    fun registerLifecycleHandler(lifeCycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }

}