package com.example.calfit.maps

import android.app.Application
import com.example.calfit.heartRate.HealthConnectManager
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class TrackingApplication: Application() {
    // 2
    private val trackingDatabase by lazy { TrackingDatabase.getDatabase(this) }
    val trackingRepository by lazy { TrackingRepository(trackingDatabase.getTrackingDao()) }

    val healthConnectManager by lazy {
        HealthConnectManager(this)
    }
}