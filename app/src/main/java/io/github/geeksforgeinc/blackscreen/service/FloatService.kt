package io.github.geeksforgeinc.blackscreen.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import io.github.geeksforgeinc.blackscreen.R
import io.github.geeksforgeinc.blackscreen.batterystats.BatteryStatsLiveData
import io.github.geeksforgeinc.blackscreen.data.model.BatteryState
import io.github.geeksforgeinc.blackscreen.databinding.ViewBlackScreenBinding
import io.github.geeksforgeinc.blackscreen.ui.BubbleTouchListener
import io.github.geeksforgeinc.blackscreen.ui.customview.BubbleRemoverView
import io.github.geeksforgeinc.blackscreen.ui.customview.BubbleView
import io.github.geeksforgeinc.blackscreen.utils.NotificationUtils
import javax.inject.Inject

@AndroidEntryPoint
class FloatService : LifecycleService() {

    companion object {
        private const val FOREGROUND_SERVICE_REQUEST_ID = 2
         const val ACTION_START_FOREGROUND_SERVICE = "startService"
         const val ACTION_STOP_FOREGROUND_SERVICE = "stopService"

        fun startService(context: Context) {
            val intent = Intent(context, FloatService::class.java).apply {
                action = ACTION_START_FOREGROUND_SERVICE
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, FloatService::class.java).apply {
                action = ACTION_STOP_FOREGROUND_SERVICE
            }
            ContextCompat.startForegroundService(context, intent)
        }
    }

    var isFirstRun = true
    @Inject
    lateinit var serviceManager : FloatServiceManager
    private lateinit var bubbleView : BubbleView
    private lateinit var bubbleRemoverView : BubbleRemoverView
    private lateinit var blackScreenBinding :  ViewBlackScreenBinding
    private lateinit var batteryStatsLiveData : BatteryStatsLiveData
    private val layoutInflater by lazy {
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreate() {
        super.onCreate()
        batteryStatsLiveData = BatteryStatsLiveData(this.applicationContext)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
                ACTION_START_FOREGROUND_SERVICE -> {
                    if (isFirstRun) {
                        isFirstRun = false
                        startForegroundService()
                        initialiseView()
                    }
                }
                ACTION_STOP_FOREGROUND_SERVICE -> {
                    cleanupViews()
                    stopForegroundService()
                }
            }
            return super.onStartCommand(intent, flags, startId)
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

    private fun stopForegroundService() {

        // Stop foreground service and remove the notification.
        stopForeground(true)

        // Stop the foreground service.
        stopSelf()
    }

    private fun cleanupViews() {
        bubbleRemoverView.hide()
        bubbleView.hide()
        blackScreenBinding.blackScreen.hide()
    }

    private fun initialiseView() {
        bubbleView = layoutInflater.inflate(R.layout.view_bubble, null) as BubbleView
        bubbleRemoverView = layoutInflater.inflate(R.layout.view_bubble_remover, null) as BubbleRemoverView
        blackScreenBinding = ViewBlackScreenBinding.inflate(layoutInflater)
        bubbleView.setOnTouchListener(BubbleTouchListener(this, bubbleRemoverView) {
            serviceManager.updateServiceStatus(false)
            stopForegroundService()
        })
        if (!bubbleView.isVisible() && !blackScreenBinding.blackScreen.isVisible()) {
            bubbleRemoverView.show()
            bubbleView.show()
        }
        blackScreenBinding.blackScreen.setOnClickListener{
            blackScreenBinding.blackScreen.hide()
        }
        bubbleView.setOnClickListener {
            blackScreenBinding.blackScreen.show()
        }

        batteryStatsLiveData.observe(this, this::handleBatteryStatsResults)
    }

    private fun handleBatteryStatsResults(batteryState: BatteryState) {
       blackScreenBinding.batteryView.chargeLevel = batteryState.batteryPercentage
       blackScreenBinding.batteryView.isCharging = batteryState.isCharging
    }


    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}