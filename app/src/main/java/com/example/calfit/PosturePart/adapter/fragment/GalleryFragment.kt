package com.oguzhancetin.goodpostureapp.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calfit.R
import com.example.calfit.databinding.FragmentGalleryBinding
import com.oguzhancetin.goodpostureapp.adapter.GalleryRcAdapter
import com.oguzhancetin.goodpostureapp.getOutputDirectory
import com.oguzhancetin.goodpostureapp.util.ItemDecoration

class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    private fun initRc() {
        val galleryAdapter = GalleryRcAdapter(getImagesUri(), ::onImageClick)
        binding.rc.apply {
            addItemDecoration(ItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_tiny)))
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(requireContext(), 4)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRc()
    }

    private fun getImagesUri(): List<Uri> {
        val uriList = arrayListOf<Uri>()
        val imageFiles = getOutputDirectory(requireActivity().application).listFiles()
        imageFiles?.let {
            it.forEach {
                uriList.add(it.toUri())
            }
        }
        return uriList
    }

    private fun onImageClick(uri: Uri) {
        val direction =
            GalleryFragmentDirections.actionGalleryFragmentToMainFragment()
        findNavController().navigate(direction)
    }

    override fun getViewBinding() = FragmentGalleryBinding.inflate(layoutInflater)
}