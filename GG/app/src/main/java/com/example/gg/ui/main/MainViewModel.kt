package com.example.gg.ui.main

import androidx.lifecycle.ViewModel
import com.example.gg.data.GameRepository
import com.example.gg.data.model.Game

class MainViewModel(private val gameRepository: GameRepository): ViewModel() {

    fun getGames(): MutableList<Game> {
        return gameRepository.getGames()
    }
}