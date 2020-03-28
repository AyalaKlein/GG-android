package com.example.gg.data

import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.data.model.Game

class GameRepository(private val dataSource: GameDataSource) {

    fun getGames(): MutableList<Game> {
        return dataSource.getGames()
    }
}