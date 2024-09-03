package dev.tools.screenlogger

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager


object ScreenLoggerHelper {
    const val ACTION_SCREEN_LOG: String = "dev.tools.screenlogger.action.SCREEN_LOG"

    const val KEY_ACTION_TAG: String = "KEY_ACTION_TAG"
    const val KEY_SCREEN_LOG: String = "KEY_SCREEN_LOG"
    const val KEY_SCREEN_LOG_TYPE: String = "KEY_SCREEN_LOG_TYPE"
    const val KEY_TIMESTAMP: String = "KEY_TIMESTAMP"

    const val SCREEN_LOG_TYPE_REQUEST: String = "Request"
    const val SCREEN_LOG_TYPE_RESPONSE: String = "Response"
    const val SCREEN_LOG_TYPE_TRACKING: String = "Tracking"
    const val SCREEN_LOG_TYPE_TEALIUM: String = "Tealium"


    fun sendBroadcast(
        context: Context?,
        actionTag: String?,
        screenLogType: String?,
        screenLog: String?
    ) {
        val broadcastIntent = Intent(ACTION_SCREEN_LOG)
        broadcastIntent.putExtra(KEY_ACTION_TAG, actionTag)
        broadcastIntent.putExtra(KEY_SCREEN_LOG_TYPE, screenLogType)
        broadcastIntent.putExtra(KEY_SCREEN_LOG, screenLog)
        broadcastIntent.putExtra(KEY_TIMESTAMP, System.currentTimeMillis())
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntent)
    }
}