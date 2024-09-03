package dev.tools.screenlogger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import dev.tools.screenlogger.R

class ScreenLoggerViewPagerAdapter(private val context: Context) : PagerAdapter() {
    private var mScreenLoggerRecyclerViewContainer: View? = null
    private val mScreenLoggerAdapter: ScreenLoggerAdapter
    private var mScreenLoggerDetailsView: ScreenLoggerDetailsView? = null
    private var mOnScreenLoggerViewPagerPageChangeListener: OnScreenLoggerViewPagerPageChangeListener? =
        null

    init {
        mScreenLoggerAdapter = ScreenLoggerAdapter(context)
    }

    override fun getCount(): Int {
        return 2
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        when (position) {
            0 -> {
                val screenLoggerRecyclerViewContainer = createScreenLoggerRecyclerViewToContainer()
                val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                val margin =
                    context.resources.getDimensionPixelSize(R.dimen.general_tiny_margin_5dp)
                layoutParams.setMargins(margin, margin, margin, margin)
                container.addView(screenLoggerRecyclerViewContainer, layoutParams)
                mScreenLoggerRecyclerViewContainer = screenLoggerRecyclerViewContainer
                return screenLoggerRecyclerViewContainer
            }

            1 -> {
                val screenLoggerDetailsView: ScreenLoggerDetailsView =
                    createScreenLoggerDetailsViewToContainer()
                val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                container.addView(screenLoggerDetailsView, layoutParams)
                mScreenLoggerDetailsView = screenLoggerDetailsView
                return screenLoggerDetailsView
            }

            else -> {
                val screenLoggerRecyclerViewContainer = createScreenLoggerRecyclerViewToContainer()
                val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                val margin =
                    context.resources.getDimensionPixelSize(R.dimen.general_tiny_margin_5dp)
                layoutParams.setMargins(margin, margin, margin, margin)
                container.addView(screenLoggerRecyclerViewContainer, layoutParams)
                mScreenLoggerRecyclerViewContainer = screenLoggerRecyclerViewContainer
                return screenLoggerRecyclerViewContainer
            }
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    private fun createScreenLoggerRecyclerViewToContainer(): View {
        val screenLoggerRecyclerViewContainer: View = LayoutInflater.from(context).inflate(
            R.layout.view_screen_logger_recycler_view_container, null, false
        )
        val btnClearLogs = screenLoggerRecyclerViewContainer.findViewById<View>(R.id.btnClearLogs)
        btnClearLogs.setOnClickListener { clearScreenLogs() }

        val screenLoggerRecyclerView: RecyclerView =
            screenLoggerRecyclerViewContainer.findViewById<RecyclerView>(R.id.mScreenLoggerRecyclerView)
        mScreenLoggerAdapter.setOnScreenLogItemClickListener(object :
            ScreenLoggerAdapter.OnScreenLogItemClickListener {
            override fun onItemClick(screenLog: ScreenLog?) {
                if (mScreenLoggerDetailsView != null) {
                    mScreenLoggerDetailsView?.setScreenLog(screenLog)
                }
                if (mOnScreenLoggerViewPagerPageChangeListener != null) {
                    mOnScreenLoggerViewPagerPageChangeListener!!.onPageChanged(1)
                }
            }
        })
        screenLoggerRecyclerView.setAdapter(mScreenLoggerAdapter)
        screenLoggerRecyclerView.setLayoutManager(LinearLayoutManager(context))

        //        appendScreenLogs(ScreenLoggerReceiver.getScreenLogs());
        val tabLayout: TabLayout = screenLoggerRecyclerViewContainer.findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setTabTextColors(
            ContextCompat.getColor(context, android.R.color.white), ContextCompat.getColor(
                context, android.R.color.white
            )
        )
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.getPosition()) {
                    0 -> mScreenLoggerAdapter.setDisplayItemType(ScreenLoggerAdapter.DisplayType.ALL)
                    1 -> mScreenLoggerAdapter.setDisplayItemType(ScreenLoggerAdapter.DisplayType.NETWORK)
                    2 -> mScreenLoggerAdapter.setDisplayItemType(ScreenLoggerAdapter.DisplayType.TRACKING)
                    else -> mScreenLoggerAdapter.setDisplayItemType(ScreenLoggerAdapter.DisplayType.Tealium)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                /*  Empty implementation to conform the protocol */
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                /*  Empty implementation to conform the protocol */
            }
        })

        return screenLoggerRecyclerViewContainer
    }

    private fun createScreenLoggerDetailsViewToContainer(): ScreenLoggerDetailsView {
        return ScreenLoggerDetailsView(context, null, object : ScreenLoggerDetailsView.OnNavigationClickListener {
            override fun onNavigationClick(v: View?) {
                if (mOnScreenLoggerViewPagerPageChangeListener != null) {
                    mOnScreenLoggerViewPagerPageChangeListener!!.onPageChanged(0)
                }
            }
        })
    }

    fun appendScreenLogs(ScreenLogs: List<ScreenLog>?) {
        mScreenLoggerAdapter.appendScreenLogs(ScreenLogs)
    }

    fun appendScreenLog(ScreenLog: ScreenLog?) {
        mScreenLoggerAdapter.appendScreenLog(ScreenLog)
    }

    fun setOnScreenLoggerViewPagerPageChangeListener(onScreenLoggerViewPagerPageChangeListener: OnScreenLoggerViewPagerPageChangeListener?) {
        mOnScreenLoggerViewPagerPageChangeListener = onScreenLoggerViewPagerPageChangeListener
    }

    fun clearScreenLogs() {
        mScreenLoggerAdapter.clearScreenLogs()
    }

    interface OnScreenLoggerViewPagerPageChangeListener {
        fun onPageChanged(position: Int)
    }
}
