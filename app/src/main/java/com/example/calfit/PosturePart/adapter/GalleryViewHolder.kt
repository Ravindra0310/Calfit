package com.oguzhancetin.goodpostureapp.adapter

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calfit.databinding.CardGalleryImageBinding

class GalleryViewHolder(
    private val binding: CardGalleryImageBinding,
    private val onCLick: (Uri) -> (Unit)
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uri: Uri) {
        Glide
            .with(binding.root)
            .load(uri)
            .centerCrop()
            .into(binding.imgImage)

        binding.imgImage.setOnClickListener {
            onCLick(uri)
        }
    }


}
