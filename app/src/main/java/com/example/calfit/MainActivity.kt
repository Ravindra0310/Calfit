package com.example.calfit

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import com.example.calfit.databinding.ActivityMainBinding
import com.example.calfit.databinding.FragmentHomeBinding
import com.example.calfit.di.SharedPreferencesManager
import com.example.calfit.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun openScreen(){
        var userDetails= SharedPreferencesManager.getUserObject(this)
        if(userDetails!=null){
            startActivity(Intent(this,HomeScreenActivity::class.java))
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}