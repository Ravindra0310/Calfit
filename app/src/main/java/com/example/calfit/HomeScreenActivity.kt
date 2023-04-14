package com.example.calfit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.calfit.databinding.ActivityHomeScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.permissionx.guolindev.PermissionX

class HomeScreenActivity : AppCompatActivity() {
    lateinit var _binding: ActivityHomeScreenBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setupWithNavController(navController)
        auth=Firebase.auth
        val currentUser = auth.currentUser
        navView.background = null
        navView.menu.getItem(2).isEnabled = false
        PermissionX.init(this)
            .permissions(android.Manifest.permission.ACTIVITY_RECOGNITION)
            .request { allGranted, grantedList, deniedList ->
            }


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.label) {
                "fragment_sub_activity" -> {
                    _binding.bottomAppBar.visibility = View.GONE
                    _binding.fab.visibility=View.GONE
                }
                "fragment_activity_details" -> {
                    _binding.bottomAppBar.visibility = View.GONE
                    _binding.fab.visibility=View.GONE
                }
                else -> {
                    _binding.bottomAppBar.visibility = View.VISIBLE
                    _binding.fab.visibility = View.VISIBLE
                }
            }
        }


    }
}