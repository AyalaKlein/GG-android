package com.example.gg.data.dbAccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.gg.data.model.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: Comment)
}