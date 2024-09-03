package dev.tools.screenlogger

import android.app.Application
import android.util.Log

class DemoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            ScreenLoggerStarter.getInstance(this)?.startLogging(applicationContext)
        } catch (ex: Exception) {
            Log.e("ScreenLogger", ex.toString())
        }


    }
}