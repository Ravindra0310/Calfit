package com.example.calfit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calfit.customUI.LivePreviewActivity
import com.example.calfit.databinding.FragmentActivityBinding
import com.example.calfit.model.Subcategory
import com.example.calfit.model.Variant
import com.example.calfit.utils.Utils
import com.google.android.gms.fitness.FitnessOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class ActivityFragment : Fragment() {

    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!
    private lateinit var fitnessOptions: FitnessOptions
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val full_body_list: String? =
            Utils.getJsonDataFromAsset(this.requireContext(), "Full_Body_Workout.json")
        val lowerBodyList: String? =
            Utils.getJsonDataFromAsset(this.requireContext(), "lower_body.json")
        val absList: String? =
            Utils.getJsonDataFromAsset(this.requireContext(), "abs_workout.json")

        val listType = object : TypeToken<List<Subcategory?>?>() {}.type
        val gson = GsonBuilder().create()
        val fullBody: ArrayList<Subcategory> = gson.fromJson(
            full_body_list,
            listType
        )
        val lowerBody: ArrayList<Subcategory> = gson.fromJson(
            full_body_list,
            listType
        )
        val absBody: ArrayList<Subcategory> = gson.fromJson(
            full_body_list,
            listType
        )
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this.requireContext(), LivePreviewActivity::class.java))
        }

        binding.ivFullbodyCard.setOnClickListener {
                findNavController().navigate(
                    ActivityFragmentDirections.actionActivityFragmentToSubActivityFragment(
                        Variant("Full Body Workout",fullBody)
                    )
                )
            }



        binding.ivAbWorkoutCard.setOnClickListener {
            findNavController().navigate(
                ActivityFragmentDirections.actionActivityFragmentToSubActivityFragment(
                    Variant("Lower Body Workout",fullBody)
                )
            )

        }

        binding.ivLowerbodyCard.setOnClickListener {
            findNavController().navigate(
                ActivityFragmentDirections.actionActivityFragmentToSubActivityFragment(
                                     Variant("Abs Workout",fullBody)
                )
            )


        }
    }
}