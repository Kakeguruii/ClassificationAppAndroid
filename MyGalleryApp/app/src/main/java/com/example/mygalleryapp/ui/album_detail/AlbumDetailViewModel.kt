package com.example.mygalleryapp.ui.album_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygalleryapp.data.Album
import com.example.mygalleryapp.data.MainDatabase
import com.example.mygalleryapp.data.Photo
import com.example.mygalleryapp.data.PhotoAlbumCrossRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(private val db: MainDatabase) : ViewModel() {
    private val _imagesList = MutableLiveData<List<Photo>>(emptyList())
    val imagesList: LiveData<List<Photo>> = _imagesList

    private val _album = MutableLiveData<Album>()
    val album: LiveData<Album> = _album

    fun init(albumId: Int) = viewModelScope.launch {
        val currentAlbum = db.galleryDao().getAlbumById(albumId)
        currentAlbum?.let {
            _imagesList.postValue(it.photos)
            _album.postValue(it.album)
        }
    }
}