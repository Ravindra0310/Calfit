package com.example.calfit.maps


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.calfit.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {
    private lateinit var binding: ActivityMapsBinding
    //private lateinit var database: Database
    // ViewModel
    private val mapsActivityViewModel: MapsActivityViewModel by viewModels {
        MapsActivityViewModelFactory(getTrackingRepository())
    }

    // Location & Map
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var polylineOptions = PolylineOptions()
    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            p0 ?: return
            p0.locations.forEach {
                val trackingEntity = TrackingEntity(Calendar.getInstance().timeInMillis, it.latitude, it.longitude)

                mapsActivityViewModel.insert(trackingEntity)
            }
        }
    }

    companion object {
        // SharedPreferences
        private const val KEY_SHARED_PREFERENCE = "com.rwRunTrackingApp.sharedPreferences"
        private const val KEY_IS_TRACKING = "com.rwRunTrackingApp.isTracking"

        // Permission
        private const val REQUEST_CODE_FINE_LOCATION = 1
        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 2
    }

    private var isTracking: Boolean
        get() = this.getSharedPreferences(KEY_SHARED_PREFERENCE, Context.MODE_PRIVATE).getBoolean(KEY_IS_TRACKING, false)
        set(value) = this.getSharedPreferences(KEY_SHARED_PREFERENCE, Context.MODE_PRIVATE).edit().putBoolean(KEY_IS_TRACKING, value).apply()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(com.example.calfit.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Set up button click events
        binding.startButton.setOnClickListener {
            // Clear the PolylineOptions from Google Map
            mMap.clear()

            // Update Start & End Button
            isTracking = true
            updateButtonStatus()

            // Reset the display text
            updateAllDisplayText(0, 0f)

            startTracking()
        }
        binding.endButton.setOnClickListener { endButtonClicked() }

        // Update layouts
        updateButtonStatus()

        // 1
        mapsActivityViewModel.allTrackingEntities.observe(this) { allTrackingEntities ->
            if (allTrackingEntities.isEmpty()) {
                updateAllDisplayText(0, 0f)
            }
        }

        // 2
        mapsActivityViewModel.lastTrackingEntity.observe(this) { lastTrackingEntity ->
            lastTrackingEntity ?: return@observe
            addLocationToRoute(lastTrackingEntity)
        }

        // 3
        mapsActivityViewModel.totalDistanceTravelled.observe(this) {
            it ?: return@observe
            val stepCount = mapsActivityViewModel.currentNumberOfStepCount.value ?: 0
            updateAllDisplayText(stepCount, it)
        }

        // 4
        mapsActivityViewModel.currentNumberOfStepCount.observe(this) {
            val totalDistanceTravelled = mapsActivityViewModel.totalDistanceTravelled.value ?: 0f
            updateAllDisplayText(it, totalDistanceTravelled)
        }

        // 5
        mapsActivityViewModel.allTrackingEntitiesRecord.observe(this) {
            addLocationListToRoute(it)
        }


        if (isTracking) {
            startTracking()
        }
    }

    // Repository
    private fun getTrackingApplicationInstance() = application as TrackingApplication
    private fun getTrackingRepository() = getTrackingApplicationInstance().trackingRepository



    // UI related codes
    private fun updateButtonStatus() {
        binding.startButton.isEnabled = !isTracking
        binding.endButton.isEnabled = isTracking
    }

    private fun updateAllDisplayText(stepCount: Int, totalDistanceTravelled: Float) {
        binding.numberOfStepTextView.text =  String.format("Step count: %d", stepCount)
        binding.totalDistanceTextView.text = String.format("Total distance: %.2fm", totalDistanceTravelled)

        val averagePace = if (stepCount != 0) totalDistanceTravelled / stepCount.toDouble() else 0.0
        binding.averagePaceTextView.text = String.format("Average pace: %.2fm/ step", averagePace)
    }

    private fun endButtonClicked() {
        AlertDialog.Builder(this)
            .setTitle("Are you sure to stop tracking?")
            .setPositiveButton("Confirm") { _, _ ->
                isTracking = false
                writeDistance()
                updateButtonStatus()
                stopTracking()
            }.setNegativeButton("Cancel") { _, _ ->
            }
            .create()
            .show()
    }

    // Tracking
    @RequiresApi(Build.VERSION_CODES.Q)
    @AfterPermissionGranted(REQUEST_CODE_ACTIVITY_RECOGNITION)
    private fun startTracking() {
        val isActivityRecognitionPermissionFree = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        val isActivityRecognitionPermissionGranted = EasyPermissions.hasPermissions(this, ACTIVITY_RECOGNITION)
        Log.d("TAG", "Is ACTIVITY_RECOGNITION permission granted $isActivityRecognitionPermissionGranted")
        if (isActivityRecognitionPermissionFree || isActivityRecognitionPermissionGranted) {
            setupStepCounterListener()
            setupLocationChangeListener()
        } else {

            EasyPermissions.requestPermissions(
                host = this,
                rationale = "For showing your step counts and calculate the average pace.",
                requestCode = REQUEST_CODE_ACTIVITY_RECOGNITION,
                perms = arrayOf(ACTIVITY_RECOGNITION)
            )
        }
    }

    private fun stopTracking() {
        polylineOptions = PolylineOptions()

        mapsActivityViewModel.deleteAllTrackingEntity()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)

        // Stop step sensor listener
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.unregisterListener(this, stepCounterSensor)
    }

    // Map related codes
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        showUserLocation()

    }

    private fun addLocationListToRoute(trackingEntityList: List<TrackingEntity>) {
        if (!this::mMap.isInitialized) {
            return
        }
        mMap.clear()
        trackingEntityList.forEach { trackingEntity ->
            val newLatLngInstance = trackingEntity.asLatLng()
            polylineOptions.points.add(newLatLngInstance)
        }
        mMap.addPolyline(polylineOptions)
    }

    private fun addLocationToRoute(trackingEntity: TrackingEntity) {
        mMap.clear()
        val newLatLngInstance = trackingEntity.asLatLng()
        polylineOptions.points.add(newLatLngInstance)
        mMap.addPolyline(polylineOptions)
    }

    // Step Counter related codes
    private fun setupStepCounterListener() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounterSensor ?: return
        sensorManager.registerListener(this@MapsActivity, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST)
    }
//    @SuppressLint("MissingPermission")
//    private fun getDeviceLocation() {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            if () {
//                val locationResult = fusedLocationProviderClient.lastLocation
//                locationResult.addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Set the map's camera position to the current location of the device.
//                        val lastKnownLocation = task.result
//                        if (lastKnownLocation != null) {
//                            mMap.moveCamera(
//                                CameraUpdateFactory.newLatLngZoom(
//                                LatLng(
//                                    lastKnownLocation.latitude,
//                                    lastKnownLocation.longitude),9.5f))
//                        }
//                    } else {
//                        Log.d("TAG", "Current location is null. Using defaults.")
//                        Log.e("TAG", "Exception: %s", task.exception)
//
//                        mMap.moveCamera(CameraUpdateFactory
//                            .newLatLngZoom(defaultLocation, 9.5f))
//                        mMap.uiSettings?.isMyLocationButtonEnabled = false
//                    }
//                }
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message, e)
//        }
//    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("TAG", "onAccuracyChanged: Sensor: $sensor; accuracy: $accuracy")
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        Log.d("TAG", "onSensorChanged")
        sensorEvent ?: return
        val firstSensorEvent = sensorEvent.values.firstOrNull() ?: return
        Log.d("TAG", "Steps count: $firstSensorEvent ")
        val isFirstStepCountRecord = mapsActivityViewModel.currentNumberOfStepCount.value == 0
        if (isFirstStepCountRecord) {
            mapsActivityViewModel.initialStepCount = firstSensorEvent.toInt()
            mapsActivityViewModel.currentNumberOfStepCount.value = 1
        } else {
            mapsActivityViewModel.currentNumberOfStepCount.value = firstSensorEvent.toInt() - mapsActivityViewModel.initialStepCount
        }
    }

    // Location
    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_FINE_LOCATION)
    private fun showUserLocation() {
        if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION)) {
            mMap.isMyLocationEnabled = true
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                host = this,
                rationale = "For showing your current location on the map.",
                requestCode = REQUEST_CODE_FINE_LOCATION,
                perms = *arrayOf(ACCESS_FINE_LOCATION)
            )
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_FINE_LOCATION)
    private fun setupLocationChangeListener() {
        if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION)) {
            val locationRequest = LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 5000 // 5000ms (5s)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                host = this,
                rationale = "For showing your current location on the map.",
                requestCode = REQUEST_CODE_FINE_LOCATION,
                perms = *arrayOf(ACCESS_FINE_LOCATION)
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }



private fun writeDistance() {
    val user=Firebase.auth.currentUser
    val stepCount = mapsActivityViewModel.currentNumberOfStepCount.value ?: 0
    val totalDistanceTravelled = mapsActivityViewModel.totalDistanceTravelled.value ?: 0f
        val database = FirebaseDatabase.getInstance("https://culfit-c4cd1-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myRef = database.getReference("Users")
    val hashMap:HashMap<Any,String> = HashMap<Any,String>()
    hashMap["distance"] = totalDistanceTravelled.toString()
    hashMap["stepCount"] = stepCount.toString()
    val currentTime = Calendar.getInstance().time
    user?.uid?.let { myRef.child(it).child(currentTime.toString()).setValue(hashMap) }
    Toast.makeText(this,"Total Distamce : ${totalDistanceTravelled}",Toast.LENGTH_LONG).show()
    Toast.makeText(this,"Total Steps : ${binding.numberOfStepTextView.text}",Toast.LENGTH_LONG).show()
    }
}