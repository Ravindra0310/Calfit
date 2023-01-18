package com.example.calfit

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.calfit.databinding.ActivityMainBinding
import com.example.calfit.di.SharedPreferencesManager
import com.example.calfit.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding.lottieMain.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
              openScreen()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {

            }

        })


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