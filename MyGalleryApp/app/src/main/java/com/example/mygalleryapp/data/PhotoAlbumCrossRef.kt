package com.example.mygalleryapp.data

import androidx.room.*

@Entity()
data class PhotoAlbumCrossRef(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val photoId: Int,
    val albumId: Int,
)

data class AlbumWithPhotos(
    @Embedded val album: Album,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "photoId",
        associateBy = Junction(PhotoAlbumCrossRef::class)
    )
    val photos: List<Photo>
)