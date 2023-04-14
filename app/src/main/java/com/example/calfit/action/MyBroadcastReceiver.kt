package com.example.calfit.action

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil.setContentView
import com.example.calfit.databinding.ActivityHomeScreenBinding
import com.example.calfit.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("Action: ${intent.action}\n")
            append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
            toString().also { log ->
                Log.d(TAG, log)

                val binding = FragmentHomeBinding.inflate(LayoutInflater.from(context))
                val view = binding.root


                Snackbar.make(view, log, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}