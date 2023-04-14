package com.example.calfit.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calfit.HomeScreenActivity
import com.example.calfit.R
import com.example.calfit.databinding.FragmentTutorial1Binding
import com.example.calfit.databinding.FragmentTutorial4Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [TutorialFragment4.newInstance] factory method to
 * create an instance of this fragment.
 */
class TutorialFragment4 : Fragment() {
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentTutorial4Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTutorial4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        binding.btnContinue.setOnClickListener {
            if (currentUser != null) {
                startActivity(Intent(this.requireContext(), HomeScreenActivity::class.java))
                this@TutorialFragment4.activity?.finish()
            } else {
                findNavController().navigate(R.id.action_tutorialFragment4_to_loginFragment)
            }
        }


    }
}