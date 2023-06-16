package com.example.mygalleryapp.ui.photos

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygalleryapp.data.Album
import com.example.mygalleryapp.data.MainDatabase
import com.example.mygalleryapp.data.Photo
import com.example.mygalleryapp.data.PhotoAlbumCrossRef
import com.example.mygalleryapp.ml.MyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.schema.ResizeBilinearOptions
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import javax.inject.Inject
import kotlin.random.Random
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

@HiltViewModel
class PhotosViewModel @Inject constructor(private val db: MainDatabase) : ViewModel() {
    private val _imagesList = MutableLiveData<List<Photo>>(emptyList())
    val imagesList: LiveData<List<Photo>> = _imagesList

    init {
        db.galleryDao().observePhotos().onEach {
            _imagesList.postValue(it)
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            db.galleryDao().insertAlbum(Album(1, "Самолеты"))
            db.galleryDao().insertAlbum(Album(2, "Машины"))
            db.galleryDao().insertAlbum(Album(3, "Птицы"))
            db.galleryDao().insertAlbum(Album(4, "Кошки"))
            db.galleryDao().insertAlbum(Album(5, "Олени"))
            db.galleryDao().insertAlbum(Album(6, "Собаки"))
            db.galleryDao().insertAlbum(Album(7, "Лягушки"))
            db.galleryDao().insertAlbum(Album(8, "Лошади"))
            db.galleryDao().insertAlbum(Album(9, "Корабли"))
            db.galleryDao().insertAlbum(Album(10, "Грузовики"))
        }
    }

    var ImageProccesor = ImageProcessor.Builder()
        .add(ResizeOp(32,32,ResizeOp.ResizeMethod.BILINEAR))
        .build()

    fun onImageSelected(imageUri: String, requireContext: Context) = viewModelScope.launch {

            val uri = imageUri.toUri()
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext.contentResolver, uri)

            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            tensorImage = ImageProccesor.process(tensorImage)

            //tensorImage = Bitmap.createScaledBitmap(bitmap, 32,32, true)

            val model = MyModel.newInstance(requireContext);

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1,32,32,3), DataType.FLOAT32);
            inputFeature0.loadBuffer(tensorImage.buffer);

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0);
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
            var maxIdx = 0
            outputFeature0.forEachIndexed { index, fl ->
                if(outputFeature0[maxIdx] < fl){
                    maxIdx = index
                }
            }

//            val outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            // Releases model resources if no longer used.
            model.close();

        /**
         * У тебя есть ссылка на фото, обрабатываешь в нейронке
         * Сохраняешь фото в базу
         * Нейронка дает тебе название альбома
         * Можешь с помощью getAlbumByName() проверить существует ли такой альбом
         * Если не существует создаешь новый Album(name = "имя из нейронки")
         * Сохраняешь его с помощью val id = db.galleryDao().insertAlbum(), функция вернет
         * id сохраненного альбома и ты его чуть позже заюзаешь
         * Если существует то из объекта который ты получила с getAlbumByName берешь albumId
         * Далее тебе нужно связать фото с альбомом
         * Создаешь PhotoAlbumCrossRef(photoId = "айди фотки созданной", albumId = "айди альбома")
         * сохраняешь в базу через insertPhotoAlbumCrossRef()
         * Все, гуд лак
         */

        val id = db.galleryDao().insertPhoto(Photo(url = imageUri))
        db.galleryDao().insertPhotoAlbumCrossRef(
            PhotoAlbumCrossRef(
                photoId = id.toInt(),
                albumId = maxIdx+1
            )
        )
    }
}