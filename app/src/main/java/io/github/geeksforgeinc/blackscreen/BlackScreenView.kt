package io.github.geeksforgeinc.blackscreen

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.widget.LinearLayout
import io.github.geeksforgeinc.blackscreen.databinding.ViewBlackScreenBinding


class BlackScreenView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var paramFloat : WindowManager.LayoutParams
    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    private val layoutInflater by lazy {
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    private val type: Int =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
    }

    init {
        ViewBlackScreenBinding.inflate(layoutInflater, this, true)

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