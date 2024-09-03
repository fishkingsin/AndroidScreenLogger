package dev.tools.screenlogger

import android.text.TextUtils
import java.io.Serializable
import java.text.SimpleDateFormat

class ScreenLog(
    val actionTag: String?,
    val screenLogType: String?,
    val screenLog: String?,
    val timestamp: Long
) : Serializable {
    val title: String
        get() = String.format(
            "%1\$s: %2\$s (%3\$s)", screenLogType, if (TextUtils.isEmpty(
                    actionTag
                )
            ) "" else actionTag,
            SimpleDateFormat(GENERAL_SCREEN_LOG_TIMESTAMP_FORMAT).format(
                timestamp
            )
        )

    companion object {
        private const val GENERAL_SCREEN_LOG_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    }
}
