package dev.tools.screenlogger

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dev.tools.screenlogger.R

class ScreenLoggerAdapter(private val mContext: Context) :
    RecyclerView.Adapter<ScreenLoggerAdapter.ScreenLoggerViewHolder?>() {
    private val mAllScreenLogs: MutableList<ScreenLog> = ArrayList<ScreenLog>()
    private var mOnScreenLogItemClickListener: OnScreenLogItemClickListener? = null
    private var mDisplayType = DisplayType.ALL
    private val mCurrentDisplayLogList: MutableList<ScreenLog> = ArrayList<ScreenLog>()

    fun appendScreenLogs(screenLogs: List<ScreenLog>?) {
        if (screenLogs != null && screenLogs.size > 0) {
            val positionStart = itemCount
            mAllScreenLogs.addAll(screenLogs)
            updateDisplayLogList()
            //            notifyItemRangeInserted(positionStart, screenLogs.size());
            notifyDataSetChanged()
        }
    }

    fun appendScreenLog(screenLog: ScreenLog?) {
        if (screenLog != null) {
            mAllScreenLogs.add(screenLog)
            if (isLogBelongsTo(screenLog, mDisplayType)) {
                mCurrentDisplayLogList.add(0, screenLog)
                //                notifyItemInserted(getItemCount() - 1);
                notifyDataSetChanged()
            }
        }
    }

    fun clearScreenLogs() {
        mAllScreenLogs.clear()
        mCurrentDisplayLogList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenLoggerViewHolder {
        return ScreenLoggerViewHolder(ScreenLoggerTextView(mContext))
    }

    override fun getItemCount(): Int {
        return mAllScreenLogs.size
    }

    override fun onBindViewHolder(holder: ScreenLoggerViewHolder, position: Int) {
        if (mCurrentDisplayLogList.size < position) {
            return
        }
        val screenLog: ScreenLog = mCurrentDisplayLogList[position]
        holder.setScreenLog(screenLog)
        if (mOnScreenLogItemClickListener != null) {
            holder.itemView.setOnClickListener(View.OnClickListener {
                mOnScreenLogItemClickListener!!.onItemClick(
                    screenLog
                )
            })
        } else {
            holder.itemView.setOnClickListener(null)
        }
    }

    private fun updateDisplayLogList() {
        mCurrentDisplayLogList.clear()
        when (mDisplayType) {
            DisplayType.ALL, DisplayType.NETWORK, DisplayType.TRACKING, DisplayType.Tealium -> for (log in mAllScreenLogs) {
                if (isLogBelongsTo(log, mDisplayType)) {
                    mCurrentDisplayLogList.add(0, log)
                }
            }
        }
    }

    private fun isLogBelongsTo(log: ScreenLog, type: DisplayType): Boolean {
        when (type) {
            DisplayType.ALL -> return true
            DisplayType.NETWORK -> if (ScreenLoggerHelper.SCREEN_LOG_TYPE_REQUEST.equals(log.screenLog) ||
                ScreenLoggerHelper.SCREEN_LOG_TYPE_RESPONSE.equals(log.screenLogType)
            ) {
                return true
            }

            DisplayType.TRACKING -> if (ScreenLoggerHelper.SCREEN_LOG_TYPE_TRACKING.equals(log.screenLogType)) {
                return true
            }

            DisplayType.Tealium -> if (ScreenLoggerHelper.SCREEN_LOG_TYPE_TEALIUM.equals(log.screenLogType)) {
                return true
            }
        }
        return false
    }

    fun setOnScreenLogItemClickListener(onScreenLogItemClickListener: OnScreenLogItemClickListener?) {
        mOnScreenLogItemClickListener = onScreenLogItemClickListener
    }

    fun setDisplayItemType(type: DisplayType) {
        mDisplayType = type
        updateDisplayLogList()
        notifyDataSetChanged()
    }

    enum class DisplayType {
        ALL,
        NETWORK,
        TRACKING,
        Tealium
    }

    interface OnScreenLogItemClickListener {
        fun onItemClick(screenLog: ScreenLog?)
    }

    inner class ScreenLoggerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLog = itemView as TextView

        fun setScreenLog(screenLog: ScreenLog) {
            tvLog.setText(screenLog.title)
        }
    }

    protected inner class ScreenLoggerTextView(context: Context) : AppCompatTextView(context) {
        init {
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            val layoutParams: RecyclerView.LayoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val margin = context.resources.getDimensionPixelSize(R.dimen.general_5dp)
            layoutParams.setMargins(margin, margin, margin, margin)
            setLayoutParams(layoutParams)
        }
    }
}
