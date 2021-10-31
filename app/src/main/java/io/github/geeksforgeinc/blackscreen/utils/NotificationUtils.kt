package io.github.geeksforgeinc.blackscreen.utils;

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import io.github.geeksforgeinc.blackscreen.R

object NotificationUtils {
    const val NOTIFICATION_ID = 2332
    const val NOTIFICATION_CHANNEL_ID = "Black Screen Alert"
    fun generateNotification (
        context: Context,
        notificationId: Int,
        channelId: String,
        channelName: String,
        pendingIntent: PendingIntent? = null,
        notificationTitle: String
    ) : Notification {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager, channelId, channelName)
        }

        val builder = NotificationCompat.Builder(context, channelId)
        pendingIntent?.let {
            builder.setContentIntent(pendingIntent)
        }
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(getNotificationIcon())
            .setContentTitle(notificationTitle)

        return  builder.build()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannel(
        notificationManager: NotificationManager,
        channelId: String,
        channelName: String
    ) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun getNotificationIcon(): Int {
        return R.mipmap.ic_launcher
    }
}
