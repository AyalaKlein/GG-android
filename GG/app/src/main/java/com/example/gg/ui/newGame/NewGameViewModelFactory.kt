package com.example.gg.ui.newGame

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gg.data.GameRepository
import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.ui.newGame.NewGameViewModel
import kotlinx.coroutines.InternalCoroutinesApi

class NewGameViewModelFactory(val context: Context): ViewModelProvider.Factory {
    @InternalCoroutinesApi
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewGameViewModel::class.java)) {
            return NewGameViewModel(
                gameRepository = GameRepository(
                    dataSource = GameDataSource(context, null)
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}