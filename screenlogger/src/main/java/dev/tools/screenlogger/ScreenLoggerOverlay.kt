package dev.tools.screenlogger


import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import dev.tools.screenlogger.R

class ScreenLoggerOverlay(val context: Context) {
    private var mScreenLoggerViewPagerAdapter: ScreenLoggerViewPagerAdapter? = null
    private var vpScreenLoggerViewPager: ViewPager? = null
    private var mCheckBox: CheckBox? = null

    init {
        init()
    }

    fun init() {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.view_screen_logger_overlay, null)
        bindView(view)
        setupView()
    }

    //    private View getView() {
    //        return mView;
    //    }
    private fun bindView(view: View) {
        vpScreenLoggerViewPager = view.findViewById<ViewPager>(R.id.vpScreenLoggerViewPager)
    }

    private fun addCheckBox(windowManager: WindowManager) {
        mCheckBox = CheckBox(context)
        val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_TOAST,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.BOTTOM or Gravity.RIGHT
        windowManager.addView(mCheckBox, layoutParams)

        mCheckBox?.setChecked(vpScreenLoggerViewPager?.getVisibility() == View.VISIBLE)

        mCheckBox?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.log_checkbox_background
            )
        )

        mCheckBox?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                if (isChecked && vpScreenLoggerViewPager?.getVisibility() == View.GONE) {
                    vpScreenLoggerViewPager?.setVisibility(View.VISIBLE)
                } else {
                    vpScreenLoggerViewPager?.setVisibility(View.GONE)
                }
            }
        })
    }

    private fun addCheckBox(container: ViewGroup) {
        mCheckBox = CheckBox(context)

        var layoutParams: ViewGroup.LayoutParams? = null
        if (container is FrameLayout) {
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.BOTTOM or Gravity.RIGHT
            layoutParams = params
        } else if (container is RelativeLayout) {
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            layoutParams = params
        }
        if (layoutParams != null) {
            container.addView(mCheckBox, layoutParams)
        }

        mCheckBox?.setChecked(vpScreenLoggerViewPager?.getVisibility() == View.VISIBLE)

        mCheckBox?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.log_checkbox_background
            )
        )

        mCheckBox?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                if (isChecked && vpScreenLoggerViewPager?.getVisibility() == View.GONE) {
                    vpScreenLoggerViewPager?.setVisibility(View.VISIBLE)
                } else {
                    vpScreenLoggerViewPager?.setVisibility(View.GONE)
                }
            }
        })
    }

    private fun setupView() {
        vpScreenLoggerViewPager?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                android.R.color.black
            )
        )
        setupViewPager()
    }

    private fun setupViewPager() {
        mScreenLoggerViewPagerAdapter = ScreenLoggerViewPagerAdapter(context)
        mScreenLoggerViewPagerAdapter?.setOnScreenLoggerViewPagerPageChangeListener(object :
            ScreenLoggerViewPagerAdapter.OnScreenLoggerViewPagerPageChangeListener {
            override fun onPageChanged(position: Int) {
                vpScreenLoggerViewPager?.setCurrentItem(position)
            }
        })
        vpScreenLoggerViewPager?.setAdapter(mScreenLoggerViewPagerAdapter)
    }

    fun appendScreenLog(ScreenLog: ScreenLog?) {
        mScreenLoggerViewPagerAdapter!!.appendScreenLog(ScreenLog)
    }

    fun addView(windowManager: WindowManager) {
        val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_TOAST,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.gravity = Gravity.BOTTOM
        windowManager.addView(vpScreenLoggerViewPager, layoutParams)

        addCheckBox(windowManager)
    }

    fun addView(activity: Activity) {
        val container = activity.findViewById<ViewGroup>(android.R.id.content)
        if (container is FrameLayout || container is RelativeLayout) {
            val params: ViewGroup.LayoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            try {

                if (vpScreenLoggerViewPager?.parent != null) {
                    return
                }
                container.addView(vpScreenLoggerViewPager, params)
                addCheckBox(container)
            } catch (e: Exception) {
                Log.d("ScreenLoggerOverlay", "addView error: $e")
            }
        }
    }

    fun removeView(windowManager: WindowManager) {
        windowManager.removeView(vpScreenLoggerViewPager)
        vpScreenLoggerViewPager = null
        windowManager.removeView(mCheckBox)
        mCheckBox = null
    }

    fun removeView(activity: Activity) {
        val container = activity.findViewById<ViewGroup>(android.R.id.content)
        container.removeView(vpScreenLoggerViewPager)
        container.removeView(mCheckBox)
    }

    fun setVisible(visible: Boolean) {
        if (visible) {
            mCheckBox?.visibility = View.VISIBLE
            if (mCheckBox?.isChecked == true) {
                vpScreenLoggerViewPager?.visibility = View.VISIBLE
            } else {
                vpScreenLoggerViewPager?.visibility = View.GONE
            }
        } else {
            vpScreenLoggerViewPager?.visibility = View.GONE
            mCheckBox?.visibility = View.GONE
        }
    }
}
