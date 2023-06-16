package com.example.mygalleryapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class Photo(
    @PrimaryKey(autoGenerate = true) val photoId: Int = 0,
    @ColumnInfo(name = "url") val url: String
)