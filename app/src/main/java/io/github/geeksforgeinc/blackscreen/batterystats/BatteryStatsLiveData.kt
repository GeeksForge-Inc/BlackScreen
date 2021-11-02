package io.github.geeksforgeinc.blackscreen.batterystats

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.geeksforgeinc.blackscreen.data.model.BatteryState

class BatteryStatsLiveData(@ApplicationContext private val context: Context) : LiveData<BatteryState>() {
    private val batteryStatsManager = BatteryStatsManager()

    override fun onActive() {
        super.onActive()
        batteryStatsManager.requestStatsUpdate(context) {
            value = it
        }
    }

    override fun onInactive() {
        super.onInactive()
        batteryStatsManager.removeUpdates(context)
    }
}