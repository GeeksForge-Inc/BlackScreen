package io.github.geeksforgeinc.blackscreen

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView


class BubbleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    private var bubbleParams: WindowManager.LayoutParams
    private val type: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
    }

    init {
    setImageResource(R.mipmap.ic_launcher_round)
        bubbleParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        bubbleParams.gravity = Gravity.CENTER
    }


    fun show() {
        if (!isAttachedToWindow) {
            windowManager.addView(this, bubbleParams)
        }
    }

    fun hide() {
        if (isAttachedToWindow) {
            windowManager.removeViewImmediate(this)
        }
    }

    fun isVisible(): Boolean {
        return isAttachedToWindow
    }
}