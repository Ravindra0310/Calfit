package com.example.calfit.action

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class DemoJobService(val listener:StepListner): JobService(), SensorEventListener {
    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        Log.e("Count","DemoJobService runs")
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounterSensor?.let {
            sensorManager.registerListener(this@DemoJobService, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
        return true;
    }

    override fun onStopJob(jobParameters: JobParameters?): Boolean {
        return true
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.e("Count","onAccuracyChanged: Sensor: $sensor; accuracy: $accuracy")
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        sensorEvent ?: return
        Log.e("Count","onSensorChanged: sensorEvent: ${sensorEvent.toString()}")
        sensorEvent.values.firstOrNull()?.let {
            Log.e("Count","Step count: $it ")
            listener.stepCount(it)
        }
    }
}