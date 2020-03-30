package com.example.gg.ui.newGame

import androidx.lifecycle.ViewModel
import com.example.gg.data.GameRepository
import com.example.gg.data.model.Game
import com.google.android.gms.tasks.Task

class NewGameViewModel(private val gameRepository: GameRepository): ViewModel() {
    fun createGame(genre: String, name: String, score: Int, description: String): Task<Void> {
        return gameRepository.createGame(genre, name, score, description)
    }
}