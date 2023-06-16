package com.example.mygalleryapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mygalleryapp.ui.albums.AlbumsFragment
import com.example.mygalleryapp.ui.photos.PhotosFragment

class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PhotosFragment()
            else -> AlbumsFragment()
        }

    }
}