package io.github.geeksforgeinc.blackscreen

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat


class FloatService : Service() {
    companion object {
        const val FOREGROUND_SERVICE_REQUEST_ID = 2
    }

    private val layoutInflater by lazy {
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return if (startId == START_STICKY) {
            startForegroundService()
            initialiseView()
            super.onStartCommand(intent, flags, startId)
        } else {
            START_NOT_STICKY
        }
    }

    private fun startForegroundService() {
        val notification = NotificationUtils.generateNotification(
            this,
            notificationId = NotificationUtils.NOTIFICATION_ID,
            channelId = NotificationUtils.NOTIFICATION_CHANNEL_ID,
            channelName = NotificationUtils.NOTIFICATION_CHANNEL_ID,
            notificationTitle = "Black Scree is enabled")

        startForeground(FOREGROUND_SERVICE_REQUEST_ID, notification)

    }

    private fun initialiseView() {
        val bubbleView = layoutInflater.inflate(R.layout.view_bubble, null) as BubbleView
        val bubbleRemoverView = layoutInflater.inflate(R.layout.view_bubble_remover, null) as BubbleRemoverView
        val blackScreenView = BlackScreenView(this)

        bubbleView.setOnTouchListener(BubbleTouchListener(this, bubbleRemoverView) {
            stopSelf()
        })

        if (!bubbleView.isVisible() && !blackScreenView.isVisible()) {
            bubbleView.show()
        }
        blackScreenView.setOnClickListener{
            blackScreenView.hide()
        }
        bubbleView.setOnClickListener {
            blackScreenView.show()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
      return null
    }
}