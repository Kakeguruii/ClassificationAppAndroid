package com.example.mygalleryapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Database(entities = [Photo::class, Album::class, PhotoAlbumCrossRef::class], version = 1)
abstract class MainDatabase : RoomDatabase() {
    abstract fun galleryDao(): GalleryDao
}

@Dao
interface GalleryDao {
    @Query("SELECT * FROM photo")
    suspend fun getPhotos(): List<Photo>

    @Query("SELECT * FROM photo")
    fun observePhotos(): Flow<List<Photo>>

    @Query("SELECT * FROM album")
    fun observeAlbums(): Flow<List<AlbumWithPhotos>>

    @Query("SELECT * FROM album WHERE albumId = :albumId")
    suspend fun getAlbumById(albumId: Int): AlbumWithPhotos?

    @Query("SELECT * FROM album WHERE name = :name")
    suspend fun getAlbumByName(name: String): AlbumWithPhotos?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotoAlbumCrossRef(ref: PhotoAlbumCrossRef)

    @Delete
    suspend fun deletePhoto(photo: Photo)

    @Delete
    suspend fun deleteAlbum(album: Album)

    @Query("DELETE FROM photo")
    suspend fun deleteAllPhotos()

    @Query("DELETE FROM album")
    suspend fun deleteAllAlbums()
}