package com.example.calfit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calfit.databinding.FragmentBmiBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class BmiFragment : Fragment() {
    private var _binding: FragmentBmiBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBmiBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth= Firebase.auth.currentUser!!
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonCalcualte.setOnClickListener {
                if (validateEdittext()) {
                    val age = binding.edittextAge.text.toString().toDouble()
                    val height = binding.edittextHeight.text.toString().toDouble()
                    val weight = binding.edittextWeight.text.toString().toDouble()
                    val solution = (weight / height / height) * 10000
                    val result:Double = String.format("%.1f", solution).toDouble()
                    binding.tvBmi.text = "BMI=${result}"
                    binding.tvBmi.visibility = View.VISIBLE
                    binding.tvBmiStatus.visibility = View.VISIBLE
                    if (result < 18) {
                        binding.tvBmiStatus.text = "UnderWeight"
                        binding.tvBmiStatus.setTextColor(
                            ContextCompat.getColor(
                                this.requireContext(),
                                R.color.red
                            )
                        )
                    } else if (result >= 18 && result < 25) {
                        binding.tvBmiStatus.text = "Normal"
                        binding.tvBmiStatus.setTextColor(
                            ContextCompat.getColor(
                                this.requireContext(),
                                R.color.green
                            )
                        )
                    } else if (result >= 25 && result < 30) {
                        binding.tvBmiStatus.text = "OverWeight"
                        binding.tvBmiStatus.setTextColor(
                            ContextCompat.getColor(
                                this.requireContext(),
                                R.color.yellow
                            )
                        )
                    } else {
                        binding.tvBmiStatus.text = "Obesity"
                        binding.tvBmiStatus.setTextColor(
                            ContextCompat.getColor(
                                this.requireContext(),
                                R.color.red
                            )
                        )
                    }
                }
            }
    }

    fun validateEdittext():Boolean{
       if(binding.edittextAge.length()==0){
           binding.edittextAge.error="Enter Age"
           return false
       }
       if(binding.edittextHeight.length()==0){
           binding.edittextHeight.error="Enter Height"
           return false
       }
       if(binding.edittextWeight.length()==0){
           binding.edittextWeight.error="Enter Weight"
           return false
       }
       if(binding.radioGroup.checkedRadioButtonId==-1){
           return false
       }
        return true
    }
}