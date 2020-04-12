package com.example.gg.data.dbAccess.dao

import androidx.room.*
import com.example.gg.data.model.Game
import com.example.gg.data.model.ModelStatus

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAll(): List<Game>

    @Query("SELECT * FROM games WHERE game_status <> 2")
    fun getDisplayGames(): List<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(game: Game)

    @Insert
    fun insert(games: MutableList<Game>)

    @Delete
    fun delete(game: Game)

    @Query("DELETE FROM games")
    fun deleteAll()
}