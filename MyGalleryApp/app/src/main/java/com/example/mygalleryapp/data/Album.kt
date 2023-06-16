package com.example.mygalleryapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album")
data class Album(
    @PrimaryKey(autoGenerate = true) val albumId: Int = 0,
    @ColumnInfo(name = "name") val name: String
)