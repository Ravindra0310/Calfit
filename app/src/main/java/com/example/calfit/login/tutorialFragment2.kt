package com.example.calfit.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calfit.R
import kotlinx.android.synthetic.main.fragment_tutorial1.*

/**
 * A simple [Fragment] subclass.
 * Use the [tutorialFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class tutorialFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial2, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_continue.setOnClickListener {
            findNavController().navigate(R.id.action_tutorialFragment2_to_tutorialFragment3)
        }
    }
}