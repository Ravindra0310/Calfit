package com.example.calfit.heartRate

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

const val MIN_SUPPORTED_SDK = Build.VERSION_CODES.O_MR1

class HealthConnectManager(private val context: Context) {

     val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

    val healthConnectCompatibleApps by lazy {
        val intent = Intent("androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE")

        // This call is deprecated in API level 33, however, this app targets a lower level.
        @Suppress("DEPRECATION")
        val packages = context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_ALL
        )

        packages.associate {
            val icon = try {
                context.packageManager.getApplicationIcon(it.activityInfo.packageName)
            } catch (e: Resources.NotFoundException) {
                null
            }
            val label = context.packageManager.getApplicationLabel(it.activityInfo.applicationInfo)
                .toString()
            it.activityInfo.packageName to HealthConnectAppInfo(
                packageName = it.activityInfo.packageName,
                icon = icon,
                appLabel = label
            )
        }
    }

    var availability = HealthConnectAvailability.NOT_SUPPORTED
    init {
        checkAvailability()
    }
    fun checkAvailability() {
        availability = when {
            HealthConnectClient.isProviderAvailable(context) -> HealthConnectAvailability.INSTALLED
            isSupported() -> HealthConnectAvailability.NOT_INSTALLED
            else -> HealthConnectAvailability.NOT_SUPPORTED
        }
    }

    suspend fun hasAllPermissions(permissions: Set<String>): Boolean {
        return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
    }

    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }
    private fun isSupported() = Build.VERSION.SDK_INT >= MIN_SUPPORTED_SDK

    suspend fun readData(
        healthConnectClient: HealthConnectClient,
    ):HealthResult {
        val now = LocalDateTime.now()
        val startTime = now.toLocalDate().atStartOfDay()
        val endTime = LocalDateTime.now()
            val response = healthConnectClient.aggregate(
                    AggregateRequest(
                        metrics = setOf(ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL,TotalCaloriesBurnedRecord.ENERGY_TOTAL,StepsRecord.COUNT_TOTAL),
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
       response[StepsRecord.COUNT_TOTAL]?.toInt()?:0
        return HealthResult(
            ACTIVE_CALORIES_TOTAL = response[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?.inKilocalories,
            ENERGY_TOTAL = response[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories,
            BPM_AVG  = response[StepsRecord.COUNT_TOTAL],
        )
    }
}

enum class HealthConnectAvailability {
    INSTALLED,
    NOT_INSTALLED,
    NOT_SUPPORTED
}