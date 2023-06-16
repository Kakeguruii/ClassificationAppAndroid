package com.example.mygalleryapp.ui.albums

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mygalleryapp.databinding.FragmentAlbumsBinding
import com.example.mygalleryapp.ui.album_detail.AlbumDetailActivity
import com.example.mygalleryapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumsFragment : BaseFragment<FragmentAlbumsBinding>(FragmentAlbumsBinding::inflate) {

    private val viewModel: AlbumsViewModel by viewModels()

    private var adapter: AlbumsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AlbumsAdapter(viewModel::onAlbumClicked, Glide.with(requireContext()))
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.albums.observe(viewLifecycleOwner) {
            adapter?.submitList(it)
        }

        viewModel.goToAlbumDetail.observe(viewLifecycleOwner) {
            val intent = Intent(requireContext(), AlbumDetailActivity::class.java)
            intent.putExtra("albumId", it)
            requireContext().startActivity(intent)
        }
    }
}