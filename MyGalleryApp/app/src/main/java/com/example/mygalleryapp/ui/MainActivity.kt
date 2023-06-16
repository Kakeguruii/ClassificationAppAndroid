package com.example.mygalleryapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mygalleryapp.R
import com.example.mygalleryapp.databinding.ActivityMainBinding
import com.example.mygalleryapp.ui.base.BaseActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initial()
    }

    private fun initial() {
        binding.viewPager.adapter = PagerAdapter(this)
        binding.tabLayout.tabIconTint = null
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.setText(R.string.tab_photos)
                1 -> tab.setText(R.string.tab_albums)
            }
        }.attach()
    }
}