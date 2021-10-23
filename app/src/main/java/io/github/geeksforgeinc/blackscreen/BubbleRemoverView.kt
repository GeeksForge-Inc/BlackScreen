package io.github.geeksforgeinc.blackscreen

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageView

class BubbleRemoverView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    private var bubbleRemoverParams : WindowManager.LayoutParams
    private val type: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
    }

    init {
        setImageResource(R.mipmap.ic_launcher_round)
        setPadding(10, 10, 10,10)
        bubbleRemoverParams  = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        bubbleRemoverParams.gravity = Gravity.BOTTOM or Gravity.CENTER
    }

    fun hide() {
        if (isVisible()) {
            windowManager.removeViewImmediate(this)
        }
    }

    fun show() {
        if (!isVisible()) {
            windowManager.addView(this, bubbleRemoverParams)
        }
    }

    fun isVisible(): Boolean {
        return isAttachedToWindow
    }
}