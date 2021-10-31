package io.github.geeksforgeinc.blackscreen.batterystats;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import io.github.geeksforgeinc.blackscreen.data.model.BatteryState

class BatteryStatsManager :  BroadcastReceiver() {
      private var batteryStatsListener : ((BatteryState) -> Unit)? = null
       override fun onReceive(context: Context?, batteryStatus: Intent?) {
           batteryStatus?.let{ intent ->
               val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
               val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
               val batteryPercent = if (level >= 0 && scale > 0)  level * 100 / scale else null

               val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
               val isCharging= status == BatteryManager.BATTERY_STATUS_CHARGING
                       || status == BatteryManager.BATTERY_STATUS_FULL
               batteryStatsListener?.invoke(
                   BatteryState(isCharging = isCharging,
                  batteryPercentage = batteryPercent
               ))
           }
       }

    fun requestStatsUpdate(context: Context, listener : (BatteryState) -> Unit) {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        batteryStatsListener = listener
        context.registerReceiver(this, filter)
    }

    fun removeUpdates(context: Context) {
        batteryStatsListener = null
        context.unregisterReceiver(this)
    }
}
