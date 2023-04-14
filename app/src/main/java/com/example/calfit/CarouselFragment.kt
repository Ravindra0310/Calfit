package com.example.calfit

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import com.bumptech.glide.Glide
import com.example.calfit.databinding.FragmentCarouselBinding


class CarouselFragment : Fragment() {
    private var _binding: FragmentCarouselBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var selectedIndex: Int = 0;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarouselBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.v1.setOnClickListener {
            if (selectedIndex == 0) return@setOnClickListener

            binding.motionContainer.setTransition(R.id.s2, R.id.s1) //orange to blue transition
            binding.motionContainer.transitionToEnd()
            selectedIndex = 0;
        }
        binding.v2.setOnClickListener {
            if (selectedIndex == 1) return@setOnClickListener

            if (selectedIndex == 2) {
                binding.motionContainer.setTransition(R.id.s3, R.id.s2)  //red to orange transition
            } else {
                binding.motionContainer.setTransition(R.id.s1, R.id.s2) //blue to orange transition
            }
            binding.motionContainer.transitionToEnd()
            selectedIndex = 1;
        }
        binding.v3.setOnClickListener {
            if (selectedIndex == 2) return@setOnClickListener

            binding.motionContainer.setTransition(R.id.s2, R.id.s3) //orange to red transition
            binding.motionContainer.transitionToEnd()
            selectedIndex = 2;
        }
        binding.button.setOnClickListener {
           if (selectedIndex==0){
               binding.motionContainer.setTransition(R.id.s1, R.id.s2) //orange to blue transition
               binding.motionContainer.transitionToEnd()
               selectedIndex = 1;
           }else if(selectedIndex==1){
               binding.motionContainer.setTransition(R.id.s2, R.id.s3)
               binding.motionContainer.transitionToEnd()
               selectedIndex = 2;
           } else if(selectedIndex==2){
               startActivity(Intent(this.context,HomeScreenActivity::class.java))
           }
        }
    }
}