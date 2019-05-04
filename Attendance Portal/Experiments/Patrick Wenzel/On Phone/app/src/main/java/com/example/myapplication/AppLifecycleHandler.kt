package com.example.myapplication

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.os.Bundle

class AppLifecycleHandler(private val lifecycleDelegate: LifecycleDelegate)
    : Application.ActivityLifecycleCallbacks, ComponentCallbacks2{

    private var appInForeground = false;
    // Override from Application.ActivityLifecycleCallbacks
    override fun onActivityResumed(p0: Activity?) {
        if (!appInForeground) {
            appInForeground = true
            lifecycleDelegate.onAppForegrounded()
        }
    }

    // Override from ComponentCallbacks2
    override fun onTrimMemory(level: Int) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            appInForeground = false
            lifecycleDelegate.onAppBackgrounded()
        }
    }

    override fun onActivityPaused(activity: Activity?){
        appInForeground = false
    }
    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        appInForeground = true
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity?) {
        appInForeground = true
    }

    override fun onActivityStopped(activity: Activity?) {
        appInForeground = false
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
    }

    override fun onLowMemory() {
    }


}