package com.example.gg.data

import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.data.model.Game
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

class GameRepository(private val dataSource: GameDataSource) {

    fun getGames(): MutableList<Game> {
        return dataSource.getGames()
    }

    fun createGame(genre: String, name: String, score: Int, description: String, uid: String): Task<String> {
        return dataSource.createGame(genre, name, score, description, uid)
    }

    fun saveImage(uid: String, data: ByteArray): UploadTask {
        return dataSource.saveImage(uid, data)
    }
}