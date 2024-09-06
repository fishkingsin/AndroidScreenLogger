package dev.tools.screenlogger

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dev.tools.screenlogger.ScreenLoggerReceiver.OnScreenLogReceiveListener
import java.util.concurrent.atomic.AtomicInteger


public class ScreenLoggerStarter private constructor(application: Application) :
    ActivityLifecycleCallbacks {
    private var mScreenLoggerOverlay: ScreenLoggerOverlay? = null
    private val mActivityOnStartedStackCount = AtomicInteger(0)
    private val mScreenLogs: MutableList<ScreenLog> = ArrayList<ScreenLog>()

    private val mScreenLoggerReceiver: BroadcastReceiver =
        ScreenLoggerReceiver(object : OnScreenLogReceiveListener {
            override fun onScreenLogReceived(screenLog: ScreenLog?) {
                if (screenLog != null) {
                    mScreenLogs.add(screenLog)
                }
                mScreenLoggerOverlay?.appendScreenLog(screenLog)
            }
        })

    init {
        application.registerActivityLifecycleCallbacks(this)
    }

    fun startLogging(context: Context) {
        addScreenLoggerUiOnWindow(
            context,
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        )
        LocalBroadcastManager.getInstance(context).registerReceiver(
            mScreenLoggerReceiver,
            IntentFilter(ScreenLoggerHelper.ACTION_SCREEN_LOG)
        )
    }

    fun stopLogging(context: Context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mScreenLoggerReceiver)
        removeScreenLoggerUi(context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
    }

    private fun addScreenLoggerUiOnWindow(context: Context, windowManager: WindowManager) {
        addScreenLoggerOverlayOnWindow(context, windowManager)
    }

    private fun removeScreenLoggerUi(windowManager: WindowManager) {
        mScreenLoggerOverlay?.removeView(windowManager)
    }

    private fun addScreenLoggerOverlayOnWindow(context: Context, windowManager: WindowManager) {
        val screenLoggerOverlay: ScreenLoggerOverlay = ScreenLoggerOverlay(context)
        //        windowManager.addView(screenLoggerOverlay, layoutParams);
        if (Build.VERSION.SDK_INT > 24) {
            screenLoggerOverlay.init()
        } else {
            if (mActivityOnStartedStackCount.incrementAndGet() > 0) {
                screenLoggerOverlay.addView(windowManager)
            }
        }
        mScreenLoggerOverlay = screenLoggerOverlay
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        /*  Empty implementation to conform the protocol */
    }

    override fun onActivityStarted(activity: Activity) {
        /*  Empty implementation to conform the protocol */
    }

    override fun onActivityResumed(activity: Activity) {
        if (mScreenLoggerOverlay == null) return
        if (Build.VERSION.SDK_INT > 24) {
            mScreenLoggerOverlay?.addView(activity)
        } else {
            if (mActivityOnStartedStackCount.incrementAndGet() > 0) {
                mScreenLoggerOverlay?.setVisible(true)
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (mScreenLoggerOverlay == null) return
        if (Build.VERSION.SDK_INT > 24) {
            mScreenLoggerOverlay?.removeView(activity)
        } else {
            if (mActivityOnStartedStackCount.decrementAndGet() == 0) {
                mScreenLoggerOverlay?.setVisible(false)
            }
        }
    }

    override fun onActivityStopped(activity: Activity) {
        /*  Empty implementation to conform the protocol */
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        /*  Empty implementation to conform the protocol */
    }

    override fun onActivityDestroyed(activity: Activity) {
        /*  Empty implementation to conform the protocol */
    }

    companion object {
        private var INSTANCE: ScreenLoggerStarter? = null
        fun getInstance(application: Application): ScreenLoggerStarter? {
            if (INSTANCE == null) {
                INSTANCE = ScreenLoggerStarter(application)
            }
            return INSTANCE
        }
    }
}
