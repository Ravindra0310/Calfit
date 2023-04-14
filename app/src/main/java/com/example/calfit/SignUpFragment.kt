package com.example.calfit

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calfit.dataModel.UserDetails
import com.example.calfit.databinding.FragmentLoginBinding
import com.example.calfit.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class SignUpFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference
        binding.buttonRegister.setOnClickListener {
            if (!binding.edittextEmail.text.isNullOrBlank() && !binding.edittextPassword.text.isNullOrBlank() && !binding.edittextFirstName.text.isNullOrBlank() && !binding.edittextLastName.text.isNullOrBlank()) {
                auth.createUserWithEmailAndPassword(
                    binding.edittextEmail.text.toString(),
                    binding.edittextPassword.text.toString()
                )
                    .addOnCompleteListener(this.requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            user?.uid?.let { it1 -> writeNewUser(it1,binding.edittextFirstName.text.toString()+" "+binding.edittextLastName.text.toString(),binding.edittextEmail.text.toString()) }
                            findNavController().navigate(R.id.action_signUpFragment_to_carouselFragment)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                this.requireContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
            }
        }

        binding.ivGoogle.setOnClickListener {

        }
    }

    private fun writeNewUser(userId: String, name: String, email: String) {
        val user = UserDetails(name, email,image = null)
        database.child("users").child(userId).setValue(user)
    }
}