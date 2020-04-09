package com.example.gg.data.dbAccess.dao

import androidx.room.*
import com.example.gg.data.model.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAll(): List<Game>

    @Insert
    fun insert(game: Game)

    @Update
    fun update(game: Game)

    @Delete
    fun delete(game: Game)
}