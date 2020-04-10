package com.example.gg.data

import android.content.Context
import android.net.Uri
import com.example.gg.data.dataSource.FireBaseDataSource
import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.data.dbAccess.dataAccess.CommentDataAccess
import com.example.gg.data.dbAccess.dataAccess.GameDataAccess
import com.example.gg.data.dbAccess.dataAccess.ImageDataAccess
import com.example.gg.data.model.Game
import com.example.gg.data.model.ModelStatus
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class GameRepository(private val dataSource: GameDataSource, context: Context) {

    private val gameLocalDB = GameDataAccess(context)
    private val imageLocalDB = ImageDataAccess(context)
    private val commentLocalDB = CommentDataAccess(context)

    fun getGames(): MutableList<Game> {
        if (FireBaseDataSource.IsConnected) {
            gameLocalDB.setGames(dataSource.getGames())
        }

        return gameLocalDB.getGames()
    }

    fun createGame(genre: String, name: String, score: Int, description: String, uid: String): Task<String> {
        val key: String = (if (FireBaseDataSource.IsConnected) FireBaseDataSource.DbRef.child("Games").push().key else UUID.randomUUID()).toString()
        val game = Game(key, genre, name, score, description, uid, ModelStatus.CREATED.ordinal)

        return if (FireBaseDataSource.IsConnected) {
            dataSource.createGame(game)
        } else {
            gameLocalDB.createGame(game)
        }
    }

    fun updateGame(key: String, genre: String, name: String, score: Int, description: String, uid: String): Task<String> {
        val game = Game(key, genre, name, score, description, uid, ModelStatus.UPDATED.ordinal)

        return if (FireBaseDataSource.IsConnected) {
            dataSource.updateGame(game)
        } else {
            gameLocalDB.updateGame(game)
        }
    }

    fun deleteGame(key: String): Task<String> {
        return if (FireBaseDataSource.IsConnected) {
            dataSource.deleteGame(key)
        } else {
            gameLocalDB.deleteGame(key)
        }
    }

    fun saveComment(gameId: String, text: String, uid: String): Task<String> {
        return if (FireBaseDataSource.IsConnected) {
            dataSource.saveComment(gameId, text, uid)
        } else {
            commentLocalDB.saveComment(gameId, text, uid)
        }
    }

    fun saveImage(uid: String, data: ByteArray): Task<String> {
        return if (FireBaseDataSource.IsConnected) {
            dataSource.saveImage(uid, data)
        } else {
            imageLocalDB.saveImage(uid, data)
        }
    }

    fun getImageUrl(uid: String): Task<ByteArray?> {
        return if (FireBaseDataSource.IsConnected) {
            dataSource.getImageUrl(uid)
        } else {
            imageLocalDB.getImageUrl(uid)
        }
    }
}