package com.example.gg.ui.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.gg.data.GameRepository
import com.example.gg.data.model.Game
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@InternalCoroutinesApi
class MainViewModel(private val gameRepository: GameRepository): ViewModel() {

    fun getGames(): MutableList<Game> {
        return gameRepository.getGames()
    }

    fun syncGames() {
        gameRepository.syncGames()
    }
}