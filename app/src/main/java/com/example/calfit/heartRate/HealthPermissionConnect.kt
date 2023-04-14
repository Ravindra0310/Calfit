package com.example.calfit.heartRate

import android.app.Activity
import android.app.LauncherActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.registerForActivityResult
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import androidx.lifecycle.lifecycleScope
import com.example.calfit.R
import com.example.calfit.databinding.ActivityHealthConnectPermssionBinding
import com.example.calfit.maps.TrackingApplication
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HealthPermissionConnect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityHealthConnectPermssionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val healthConnectClient = (application as TrackingApplication).healthConnectManager

        lifecycleScope.launch {
            checkPermissionsAndRun(healthConnectClient.healthConnectClient)
        }

    }
    val permissions = setOf(
        HealthPermission.getWritePermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class),
        HealthPermission.getWritePermission(SpeedRecord::class),
        HealthPermission.getWritePermission(DistanceRecord::class),
        HealthPermission.getWritePermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getWritePermission(HeartRateRecord::class)
    )
    private val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

    private val requestPermissions =
        registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(permissions)) {
               Toast.makeText(this,"sucess",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"fail",Toast.LENGTH_SHORT).show()

            }
        }

    suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        if (granted.containsAll(permissions)) {
            Toast.makeText(this,"all permission granted",Toast.LENGTH_SHORT).show()
        } else {
            requestPermissions.launch(permissions)
        }
    }
}