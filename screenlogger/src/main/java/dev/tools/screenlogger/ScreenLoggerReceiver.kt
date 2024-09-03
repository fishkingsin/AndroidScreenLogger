package dev.tools.screenlogger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenLoggerReceiver(private val mOnScreenLogReceiveListener: OnScreenLogReceiveListener?) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null) {
            val actionTag = intent.getStringExtra(ScreenLoggerHelper.KEY_ACTION_TAG)
            val screenLog = intent.getStringExtra(ScreenLoggerHelper.KEY_SCREEN_LOG)
            val screenLogType = intent.getStringExtra(ScreenLoggerHelper.KEY_SCREEN_LOG_TYPE)
            val timestamp = intent.getLongExtra(ScreenLoggerHelper.KEY_TIMESTAMP, 0)
            val screenLogObject: ScreenLog = ScreenLog(
                actionTag,
                screenLogType,
                screenLog,
                timestamp
            )
            mOnScreenLogReceiveListener?.onScreenLogReceived(screenLogObject)
        }
    }

    interface OnScreenLogReceiveListener {
        fun onScreenLogReceived(screenLog: ScreenLog?)
    }
}