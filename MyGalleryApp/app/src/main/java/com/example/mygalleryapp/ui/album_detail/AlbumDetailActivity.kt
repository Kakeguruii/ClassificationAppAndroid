package com.example.mygalleryapp.ui.album_detail

import android.R
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.NavUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mygalleryapp.databinding.ActivityAlbumDetailBinding
import com.example.mygalleryapp.ui.ImageAdapter
import com.example.mygalleryapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlbumDetailActivity :
    BaseActivity<ActivityAlbumDetailBinding>(ActivityAlbumDetailBinding::inflate) {

    private val adapter = ImageAdapter()

    private val viewModel: AlbumDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.imagesList.observe(this) {
            adapter.submitImages(it)
        }

        viewModel.album.observe(this) {
            supportActionBar?.title = it.name
        }

        val albumId = intent.getIntExtra("albumId", 0)
        viewModel.init(albumId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
