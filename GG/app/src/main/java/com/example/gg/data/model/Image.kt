package com.example.gg.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "data") var data: String = ""
) {
}