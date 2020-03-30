package com.example.gg.ui.newGame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gg.data.GameRepository
import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.ui.newGame.NewGameViewModel

class NewGameViewModelFactory(): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewGameViewModel::class.java)) {
            return NewGameViewModel(
                gameRepository = GameRepository(
                    dataSource = GameDataSource(null)
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}