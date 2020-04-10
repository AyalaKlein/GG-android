package com.example.gg.data

import android.net.Uri
import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.data.model.Game
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class GameRepository(private val dataSource: GameDataSource) {

    fun getGames(): MutableList<Game> {
        return dataSource.getGames()
    }

    fun createGame(genre: String, name: String, score: Int, description: String, uid: String): Task<String> {
        return dataSource.createGame(genre, name, score, description, uid)
    }

    fun updateGame(key: String, genre: String, name: String, score: Int, description: String, uid: String): Task<String> {
        return dataSource.updateGame(key, genre, name, score, description, uid)
    }

    fun deleteGame(key: String): Task<String> {
        return dataSource.deleteGame(key)
    }

    fun saveComment(gameId: String, text: String, uid: String): Task<String> {
        return dataSource.saveComment(gameId, text, uid)
    }

    fun saveImage(uid: String, data: ByteArray): Task<String> {
        return dataSource.saveImage(uid, data)
    }

    fun getImageUrl(uid: String): Task<ByteArray?> {
        return dataSource.getImageUrl(uid)
    }
}