package io.github.geeksforgeinc.blackscreen.ui.customview

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout


class FloatConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var paramFloat : WindowManager.LayoutParams
    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val type: Int =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
    }

    init {
        paramFloat = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            type,
            65794,
            PixelFormat.TRANSPARENT
        )
        paramFloat.gravity = Gravity.CENTER
    }

    fun show() {
        if (!isAttachedToWindow) {
            windowManager.addView(this, paramFloat)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                windowInsetsController?.hide(WindowInsets.Type.statusBars())
                windowInsetsController?.hide(WindowInsets.Type.navigationBars())
            } else {
                systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
            }
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