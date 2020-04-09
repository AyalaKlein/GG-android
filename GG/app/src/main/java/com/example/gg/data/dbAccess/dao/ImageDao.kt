package com.example.gg.data.dbAccess.dao

import androidx.room.*
import com.example.gg.data.model.Image

@Dao
interface ImageDao {
    @Query("SELECT * FROM images WHERE id = :id")
    fun get(id: String): Image?

    @Query("SELECT * FROM images WHERE id in (:ids)")
    fun get(ids: Array<String>): MutableList<Image>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: Image)

    @Delete
    fun delete(image: Image)
}