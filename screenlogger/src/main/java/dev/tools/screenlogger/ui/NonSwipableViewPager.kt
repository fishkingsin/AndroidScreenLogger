package dev.tools.screenlogger.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class NonSwipableViewPager : ViewPager {
    private var mIsSwipable = false

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (mIsSwipable) {
            super.onInterceptTouchEvent(event)
        } else {
            false
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mIsSwipable) {
            super.onTouchEvent(event)
        } else {
            false
        }
    }

    fun setSwipable(swipable: Boolean) {
        mIsSwipable = swipable
    }
}
