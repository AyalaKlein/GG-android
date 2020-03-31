package com.example.gg.ui.newGame

import androidx.lifecycle.ViewModel
import com.example.gg.data.GameRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

class NewGameViewModel(private val gameRepository: GameRepository): ViewModel() {
    fun createGame(genre: String, name: String, score: Int, description: String, uid: String): Task<String> {
        return gameRepository.createGame(genre, name, score, description, uid)
    }

    fun updateGame(key: String, genre: String, name: String, score: Int, description: String, uid: String): Task<String> {
        return gameRepository.updateGame(key, genre, name, score, description, uid)
    }

    fun saveImage(uid: String, data: ByteArray): UploadTask {
        return gameRepository.saveImage(uid, data)
    }
}