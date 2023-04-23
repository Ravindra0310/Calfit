package com.example.calfit


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import androidx.health.connect.client.units.percent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.calfit.databinding.FragmentHomeBinding
import com.example.calfit.heartRate.HealthConnectManager
import com.example.calfit.heartRate.HealthPermissionConnect
import com.example.calfit.heartRate.HealthResult
import com.example.calfit.maps.MapsActivity
import com.example.calfit.maps.TrackingApplication
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.oguzhancetin.goodpostureapp.PostureActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fitnessOptions: FitnessOptions
    private lateinit var auth: FirebaseAuth
    private lateinit var  heathConnectData:HealthResult

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        binding.textView7.text = auth.currentUser?.displayName
        binding.imageView4.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bmiFragment)
        }
        binding.imageView5.setOnClickListener {
        }
//        val sensorManager = this.requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//        stepCounterSensor?.let {
//            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
//        }
        readFitness()
        getStepCount()
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this.requireContext(), MapsActivity::class.java))
        }



        val healthConnectClient = (this.requireActivity().application as TrackingApplication).healthConnectManager

        lifecycleScope.launch {
            checkPermissionsAndRun(healthConnectClient.healthConnectClient)
            heathConnectData=healthConnectClient.readData(healthConnectClient.healthConnectClient)
            Log.e("Health",heathConnectData.toString())


            withContext(Dispatchers.Main){
                val percentage = (heathConnectData.ENERGY_TOTAL?.times(100))?.div(6000)
                binding.progressBarCal.progress = percentage?.toInt()!!
                binding.txtProgressCal.text = heathConnectData.ENERGY_TOTAL?.toInt().toString()+"\n Cal"
            }
        }
    }


//    override fun onSensorChanged(event: SensorEvent?) {
//        event ?: return
//        // Data 1: According to official documentation, the first value of the `SensorEvent` value is the step count
//        event.values.firstOrNull()?.let {
//            val percentage = (it/10000) * 100
//            binding.progressBar.progress= percentage.toInt()
//            binding.txtProgress.text= it.toInt().toString()
//        }
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//       Log.e("Message","onAccuracyChanged: Sensor: $sensor; accuracy: $accuracy")
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun readFitness() {
        fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
        val account = GoogleSignIn.getAccountForExtension(this.requireContext(), fitnessOptions)

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this, // your activity
                11,
                account,
                fitnessOptions
            )
        } else {
           getStepCount()
        }
    }



    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getStepCount() {
        val startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault())
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())

        val datasource = DataSource.Builder()
            .setAppPackageName("com.google.android.gms")
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setType(DataSource.TYPE_DERIVED)
            .setStreamName("estimated_steps")
            .build()

        val request = DataReadRequest.Builder()
            .aggregate(datasource)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
            .build()

        Fitness.getHistoryClient(
            this.requireContext(),
            GoogleSignIn.getAccountForExtension(this.requireContext(), fitnessOptions)
        )
            .readData(request)
            .addOnSuccessListener { response ->
                val totalSteps = response.buckets
                    .flatMap { it.dataSets }
                    .flatMap { it.dataPoints }
                    .sumBy { it.getValue(Field.FIELD_STEPS).asInt() }
                Log.i(TAG, "Total steps: $totalSteps")
                val percentage = (totalSteps.toDouble()*100) / 6000
                binding.progressBar.progress =percentage.toInt()
                binding.txtProgress.text = totalSteps.toString()+"\n Steps"
            }


    }

    val permissions = setOf(
        HealthPermission.getWritePermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),


    )
    private val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

    private val requestPermissions =
        registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(permissions)) {
                Toast.makeText(this.requireContext(),"sucess", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.requireContext(),"fail", Toast.LENGTH_SHORT).show()

            }
        }

    suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        if (granted.containsAll(permissions)) {
            Toast.makeText(this.requireContext(),"all permission granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissions.launch(permissions)
        }
    }
}
