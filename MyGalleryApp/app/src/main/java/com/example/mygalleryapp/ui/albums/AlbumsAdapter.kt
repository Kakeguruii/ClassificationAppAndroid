package com.example.mygalleryapp.ui.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.mygalleryapp.data.AlbumWithPhotos
import com.example.mygalleryapp.databinding.ItemAlbumBinding

class AlbumsAdapter(private val onClick: (Int) -> Unit, private val glide: RequestManager) :
    ListAdapter<AlbumWithPhotos, AlbumsAdapter.AlbumsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class AlbumsViewHolder(
        private val binding: ItemAlbumBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: AlbumWithPhotos) {
            binding.apply {
                tvAlbumTitle.text = album.album.name
                flAlbum.setOnClickListener { onClick(album.album.albumId) }
                val photo = album.photos.lastOrNull()?.url
                photo?.let {
                    glide.load(album.photos.last().url).into(binding.ivAlbumPhoto)
                } ?: binding.ivAlbumPhoto.setImageResource(0)
                }
            }
        }

    class DiffCallback : DiffUtil.ItemCallback<AlbumWithPhotos>() {
        override fun areItemsTheSame(oldItem: AlbumWithPhotos, newItem: AlbumWithPhotos) =
            oldItem.album.albumId == newItem.album.albumId

        override fun areContentsTheSame(oldItem: AlbumWithPhotos, newItem: AlbumWithPhotos) =
            oldItem == newItem
    }
}