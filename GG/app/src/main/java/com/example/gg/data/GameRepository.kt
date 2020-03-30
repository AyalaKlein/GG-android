package com.example.gg.data

import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.data.model.Game
import com.google.android.gms.tasks.Task

class GameRepository(private val dataSource: GameDataSource) {

    fun getGames(): MutableList<Game> {
        return dataSource.getGames()
    }

    fun createGame(genre: String, name: String, score: Int, description: String): Task<Void> {
        return dataSource.createGame(genre, name, score, description)
    }
}