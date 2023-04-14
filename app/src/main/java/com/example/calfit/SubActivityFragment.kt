package com.example.calfit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calfit.adapter.ExerciseAdapter
import com.example.calfit.databinding.FragmentActivityBinding
import com.example.calfit.databinding.FragmentSubActivityBinding
import com.example.calfit.model.Subcategory
import com.example.calfit.model.Variant


class SubActivityFragment : Fragment(),ExerciseAdapter.ClickedListener {
    private var _binding: FragmentSubActivityBinding? = null
    private val binding get() = _binding!!
    val args:SubActivityFragmentArgs by navArgs()
    private lateinit var variants:Variant
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        variants=args.variant
        binding.rvExercise.adapter= variants.subcategories?.let { ExerciseAdapter(this, it) }
        binding.rvExercise.layoutManager=LinearLayoutManager(this.requireContext())
        binding.tvTitle.text=variants.name

        binding.btnStart.setOnClickListener{
                    findNavController().navigate(SubActivityFragmentDirections.actionSubActivityFragmentToActivityDetailsFragment(variants))
        }
    }

    override fun onClicked(position: Int,data:Subcategory) {
//        findNavController().navigate(SubActivityFragmentDirections.actionSubActivityFragmentToActivityDetailsFragment(data))
    }
}