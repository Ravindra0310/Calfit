package com.example.calfit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.calfit.databinding.FragmentProfileBinding
import com.example.calfit.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        database =
            FirebaseDatabase.getInstance("https://culfit-c4cd1-default-rtdb.asia-southeast1.firebasedatabase.app")
        myRef = database.getReference("Users")
        if (auth.currentUser != null) {
           binding.tvName.text= auth.currentUser!!.displayName
                Glide.with(binding.ivProfilePic).load(auth.currentUser!!.photoUrl).circleCrop().into(binding.ivProfilePic)
            val database =
                FirebaseDatabase.getInstance("https://culfit-c4cd1-default-rtdb.asia-southeast1.firebasedatabase.app")
            val myRef = database.getReference("Users")
            myRef.child(auth.currentUser!!.uid).child("UserData").get().addOnSuccessListener {
                val data=it.value as Map<String, String>
                binding.edittextAge.setText(data["age"])
                binding.edittextHeight.setText(data["height"])
                binding.edittextWeight.setText(data["weight"])

            }

        }

        binding.btnSave.setOnClickListener {
            saveUserData()
        }

        binding.btnLoginOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this.requireContext(),LoginActivity::class.java))
            this.requireActivity().finish()
        }
    }

    fun saveUserData() {
        val hashMap: HashMap<String, String> = HashMap<String, String>()
        if (validateEdittext()) {
            hashMap["age"] = binding.edittextAge.text.toString()
            hashMap["height"] = binding.edittextHeight.text.toString()
            hashMap["weight"] = binding.edittextWeight.text.toString()
            hashMap["gender"] =
                if (binding.radioGroup.checkedRadioButtonId == 1) "Male" else "Female"
            auth.currentUser?.uid?.let { myRef.child(it).child("UserData").setValue(hashMap) }
        }
    }


    fun validateEdittext(): Boolean {
        if (binding.edittextAge.length() == 0) {
            binding.edittextAge.error = "Enter Age"
            return false
        }
        if (binding.edittextHeight.length() == 0) {
            binding.edittextHeight.error = "Enter Height"
            return false
        }
        if (binding.edittextWeight.length() == 0) {
            binding.edittextWeight.error = "Enter Weight"
            return false
        }
        if (binding.radioGroup.checkedRadioButtonId == -1) {
            return false
        }
        return true
    }
}

