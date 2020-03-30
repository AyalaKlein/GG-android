package com.example.gg.ui.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.gg.data.GameRepository
import com.example.gg.data.model.Game
import com.google.android.gms.tasks.Task

class MainViewModel(private val gameRepository: GameRepository): ViewModel(), ImageRetriever {

    fun getGames(): MutableList<Game> {
        return gameRepository.getGames()
    }

    override fun getImageUrl(uid: String): Task<Uri> {
        return gameRepository.getImageUrl(uid)
    }
}