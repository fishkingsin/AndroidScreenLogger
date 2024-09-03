package dev.tools.screenlogger

import android.content.ClipboardManager
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import dev.tools.screenlogger.R


class ScreenLoggerDetailsView(
    context: Context,
    attributeSet: AttributeSet?,
    private val mOnNavigationClickListener: OnNavigationClickListener?
) : RelativeLayout(context) {
    private var tbScreenLogDetails: Toolbar? = null
    private var tvScreenLogDetails: TextView? = null
    private var tvToolbar: TextView? = null
    private var view: View? = null
    private var mScreenLog: ScreenLog? = null

    init {
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        view =
            LayoutInflater.from(getContext()).inflate(R.layout.view_screen_log_details, this, true)
        bindView()
        setupNavigationIcon()
        setScreenLogText()
    }

    private fun bindView() {
        tbScreenLogDetails = view!!.findViewById<Toolbar>(R.id.tbScreenLoggerDetails)
        tvScreenLogDetails = view!!.findViewById<TextView>(R.id.tvScreenLoggerDetails)
        tvScreenLogDetails?.setTextIsSelectable(true)
        tvScreenLogDetails?.setOnLongClickListener(OnLongClickListener { v: View? ->
            val clipboard =
                getContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = tvScreenLogDetails?.getText()
            Toast.makeText(getContext(), "text is copied", Toast.LENGTH_LONG).show()
            true
        })
        tvScreenLogDetails?.setMovementMethod(ScrollingMovementMethod())
        tvToolbar = view!!.findViewById<TextView>(R.id.tvToolbar)
    }

    fun setScreenLog(screenLog: ScreenLog?) {
        mScreenLog = screenLog
        setTitle()
        setScreenLogText()
    }

    private fun setupNavigationIcon() {
        tbScreenLogDetails?.setNavigationIcon(
            ContextCompat.getDrawable(context, R.drawable.btn_general_back)
        )
        if (mOnNavigationClickListener != null) {
            tbScreenLogDetails!!.setNavigationOnClickListener { v ->
                mOnNavigationClickListener.onNavigationClick(
                    v
                )
            }
        }
    }

    private fun setTitle() {
        tvToolbar?.text = mScreenLog?.title
    }

    private fun setScreenLogText() {
        var screenLog = ""
        if (mScreenLog != null) {
            screenLog = mScreenLog?.screenLog.toString()
        }
        tvScreenLogDetails?.text = StringUtil.beautifyJsonString(screenLog)
        tvScreenLogDetails!!.scrollTo(0, 0)
    }

    interface OnNavigationClickListener {
        fun onNavigationClick(v: View?)
    }
}
