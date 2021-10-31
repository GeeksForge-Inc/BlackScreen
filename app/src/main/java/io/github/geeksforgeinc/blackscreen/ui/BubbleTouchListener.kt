package io.github.geeksforgeinc.blackscreen.ui

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import io.github.geeksforgeinc.blackscreen.ui.customview.BubbleView
import io.github.geeksforgeinc.blackscreen.ui.customview.BubbleRemoverView
import kotlin.math.sqrt

/**
 *  Three major functionalities are working in this touch listener,
 *   1. drag the bubble view
 *   2. remove bubble view when it overlaps bubbleRemoverView
 *   3. click listener for bubble view
 *
 *   I have attached the references which helped me to build the above functionalities below
 *   1. https://github.com/elye/demo_android_windowmanager
 *   2. https://github.com/DisionCo/android-facebook-like-chat-head
 *   3. https://stackoverflow.com/a/29933115/9127903
 */
class BubbleTouchListener(
    private val context: Context,
    private val bubbleRemover: BubbleRemoverView,
    private val onBubbleRemoved: (() -> Unit)
) : View.OnTouchListener {

    companion object {
        /**
         * Max allowed duration for a "click", in milliseconds.
         */
        private const val MAX_CLICK_DURATION = 1000

        /**
         * Max allowed distance to move during a "click", in DP.
         */
        private const val MAX_CLICK_DISTANCE = 15

        private fun pxToDp(px: Float, context: Context): Float {
            return px / context.resources.displayMetrics.density
        }

        private fun distance(context: Context, x1: Float, y1: Float, x2: Float, y2: Float): Float {
            val dx = x1 - x2
            val dy = y1 - y2
            val distanceInPx = sqrt((dx * dx + dy * dy).toDouble()).toFloat()
            return pxToDp(distanceInPx, context)
        }
    }

    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    private var xInitCord: Int = 0
    private var yInitCord: Int = 0
    private var xInitMargin: Int = 0
    private var yInitMargin: Int = 0
    private var pressStartTime: Long = 0
    private var pressedX = 0f
    private var pressedY = 0f
    private var stayedWithinClickDistance = false

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val xCord = event.rawX.toInt()
        val yCord = event.rawY.toInt()
        val xCordDestination: Int
        val yCordDestination: Int
        val bubble = view as BubbleView
        val bubbleParams = bubble.layoutParams as WindowManager.LayoutParams
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                xInitCord = xCord
                yInitCord = yCord
                xInitMargin = bubbleParams.x
                yInitMargin = bubbleParams.y
                // for tracking clicks on bubble view
                pressStartTime = System.currentTimeMillis();
                pressedX = event.x
                pressedY = event.y
                stayedWithinClickDistance = true
                // add recycle bin when moving crumpled paper
                bubbleRemover.visibility = View.VISIBLE
            }
            MotionEvent.ACTION_UP -> {

                if (isViewsOverlapping(bubble, bubbleRemover)) {
                    bubble.hide()
                    bubbleRemover.hide()
                    onBubbleRemoved()
                } else {
                    val pressDuration: Long = System.currentTimeMillis() - pressStartTime
                    if (pressDuration < MAX_CLICK_DURATION &&
                        distance(context, pressedX, pressedY, event.x, event.y) < MAX_CLICK_DISTANCE &&
                        stayedWithinClickDistance) {
                        // Click event has occurred
                        bubble.performClick()
                    }
                }

                // always remove recycle bin ImageView when paper is dropped
                bubbleRemover.visibility = View.GONE
            }
            MotionEvent.ACTION_MOVE -> {
                if (stayedWithinClickDistance &&
                    distance(context, pressedX, pressedY, event.x, event.y) > MAX_CLICK_DISTANCE
                ) {
                    stayedWithinClickDistance = false;
                }
                val xDiffMove: Int = xCord - xInitCord
                val yDiffMove: Int = yCord - yInitCord
                xCordDestination = xInitMargin + xDiffMove
                yCordDestination = yInitMargin + yDiffMove

                bubbleParams.x = xCordDestination
                bubbleParams.y = yCordDestination
                windowManager.updateViewLayout(bubble, bubbleParams)
            }
        }
        return true
    }

    private fun isViewsOverlapping(firstView: View, secondView: View): Boolean {
        val firstPosition = IntArray(2)
        val secondPosition = IntArray(2)
        firstView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        firstView.getLocationOnScreen(firstPosition)
        secondView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        secondView.getLocationOnScreen(secondPosition)
        return firstPosition[0] < secondPosition[0] + secondView.measuredWidth &&
                firstPosition[0] + firstView.measuredWidth > secondPosition[0] &&
                firstPosition[1] < secondPosition[1] + secondView.measuredHeight &&
                firstPosition[1] + firstView.measuredHeight > secondPosition[1]
    }


}