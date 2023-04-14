package com.example.calfit.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calfit.R
import com.example.calfit.databinding.FragmentTutorial1Binding
import com.example.calfit.databinding.FragmentTutorial2Binding


/**
 * A simple [Fragment] subclass.
 * Use the [tutorialFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class tutorialFragment2 : Fragment() {

    private var _binding: FragmentTutorial2Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTutorial2Binding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_tutorialFragment2_to_tutorialFragment3)
        }
    }
}