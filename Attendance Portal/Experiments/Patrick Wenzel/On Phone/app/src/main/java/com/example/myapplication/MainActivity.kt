package com.example.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity(), LifecycleDelegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lifeCycleHandler = AppLifecycleHandler(this)
        val a = App()
        a.registerLifecycleHandler(lifeCycleHandler)
    }
        override fun onAppBackgrounded() {
            Log.d("Awww", "App in background")
        }

        override fun onAppForegrounded() {
            Log.d("Yeeey", "App in foreground")
        }
}
