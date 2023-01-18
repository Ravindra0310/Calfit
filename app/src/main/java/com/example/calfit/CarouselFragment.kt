package com.example.calfit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.fragment_carousel.*
import kotlinx.android.synthetic.main.views.*

class CarouselFragment : Fragment() {

    private var selectedIndex: Int = 0;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carousel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        v1.setOnClickListener {
            if (selectedIndex == 0) return@setOnClickListener

            motion_container.setTransition(R.id.s2, R.id.s1) //orange to blue transition
            motion_container.transitionToEnd()
            selectedIndex = 0;
        }
        v2.setOnClickListener {
            if (selectedIndex == 1) return@setOnClickListener

            if (selectedIndex == 2) {
                motion_container.setTransition(R.id.s3, R.id.s2)  //red to orange transition
            } else {
                motion_container.setTransition(R.id.s1, R.id.s2) //blue to orange transition
            }
            motion_container.transitionToEnd()
            selectedIndex = 1;
        }
        v3.setOnClickListener {
            if (selectedIndex == 2) return@setOnClickListener

            motion_container.setTransition(R.id.s2, R.id.s3) //orange to red transition
            motion_container.transitionToEnd()
            selectedIndex = 2;
        }
    }
}