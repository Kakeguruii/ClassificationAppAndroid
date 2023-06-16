package com.example.mygalleryapp.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygalleryapp.data.AlbumWithPhotos
import com.example.mygalleryapp.data.MainDatabase
import com.example.mygalleryapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(private val db: MainDatabase) : ViewModel() {

    private val _albums = MutableLiveData<List<AlbumWithPhotos>>()
    val albums: LiveData<List<AlbumWithPhotos>> = _albums

    private val _goToAlbumDetail = SingleLiveEvent<Int>()
    val goToAlbumDetail: LiveData<Int> = _goToAlbumDetail

    init {
        db.galleryDao().observeAlbums().onEach {
            _albums.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun onAlbumClicked(albumId: Int) {
        _goToAlbumDetail.postValue(albumId)
    }
}