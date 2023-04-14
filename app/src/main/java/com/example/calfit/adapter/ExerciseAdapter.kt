package com.example.calfit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calfit.databinding.ItemExerciseBinding
import com.example.calfit.model.Subcategory
import com.example.calfit.model.Variant

class ExerciseAdapter(
    private val listener: ClickedListener,
    var list: ArrayList<Subcategory>
) : RecyclerView.Adapter<ExerciseViewHolder>() {
    private var selectedPosition = 0

    interface ClickedListener {
        fun onClicked(position: Int,data:Subcategory)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding)
    }
    fun setSelectedIndex(i: Int) {
        selectedPosition = i
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val data=list[position]
        with(holder.binding){
            Glide.with(this.imageView).load(data.images).into(this.imageView)
            this.tvTitle.text=data.title
            this.tvTime.text=data.duration?.toDouble().toString()
            this.ibContinue.setOnClickListener {
                listener.onClicked(position,data)
            }
        }

    }

}

class ExerciseViewHolder(val binding: ItemExerciseBinding) :
    RecyclerView.ViewHolder(binding.root)