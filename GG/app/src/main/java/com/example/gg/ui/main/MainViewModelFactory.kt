package com.example.gg.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gg.data.GameRepository
import com.example.gg.data.dataSource.GameDataSource

class MainViewModelFactory(val callback: (Unit)->Unit): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                    gameRepository = GameRepository(
                    dataSource = GameDataSource {
                        callback(Unit)
                    }
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}