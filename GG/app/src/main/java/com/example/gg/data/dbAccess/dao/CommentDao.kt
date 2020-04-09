package com.example.gg.data.dbAccess.dao

import androidx.room.*
import com.example.gg.data.model.Comment

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments")
    fun getAll(): List<Comment>

    @Insert
    fun insert(comment: Comment)

    @Update
    fun update(comment: Comment)

    @Delete
    fun delete(comment: Comment)
}