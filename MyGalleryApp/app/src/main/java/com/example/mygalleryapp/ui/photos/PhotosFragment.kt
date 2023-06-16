package com.example.mygalleryapp.ui.photos

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mygalleryapp.databinding.FragmentPhotosBinding
import com.example.mygalleryapp.ui.ImageAdapter
import com.example.mygalleryapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosFragment : BaseFragment<FragmentPhotosBinding>(FragmentPhotosBinding::inflate) {

    companion object {
        private const val PICK_IMAGES_REQUEST_CODE = 101
    }

    private val adapter = ImageAdapter()

    private val viewModel: PhotosViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = adapter
        binding.addImg.setOnClickListener {
            selectImagesFromGallery()
        }

        viewModel.imagesList.observe(viewLifecycleOwner) {
            adapter.submitImages(it)
        }
    }


    private fun selectImagesFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            Intent.createChooser(intent, "Select Images"),
            PICK_IMAGES_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        requireContext().contentResolver

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if (data.clipData != null) {
                for (i in 0 until data.clipData!!.itemCount) {
                    val imageUri = data.clipData!!.getItemAt(i).uri.toString()
                    viewModel.onImageSelected(imageUri,  requireContext())
                }
            } else {
                val imageUri = data.data.toString()
                viewModel.onImageSelected(imageUri,  requireContext())
            }
        }
    }
}
