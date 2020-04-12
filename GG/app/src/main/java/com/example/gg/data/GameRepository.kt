package com.example.gg.data

import android.content.Context
import com.example.gg.data.dataSource.FireBaseDataSource
import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.data.dbAccess.dataAccess.CommentDataAccess
import com.example.gg.data.dbAccess.dataAccess.GameDataAccess
import com.example.gg.data.internetConnection.AppInternetConnection
import com.example.gg.data.model.Comment
import com.example.gg.data.model.Game
import com.example.gg.data.model.ModelStatus
import com.example.gg.data.sync.SyncManager
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.util.*

@ObsoleteCoroutinesApi
@InternalCoroutinesApi
class GameRepository(private val dataSource: GameDataSource, private val context: Context) {

    private val gameLocalDB = GameDataAccess(context)
    private val commentLocalDB = CommentDataAccess(context)
    private val internetTest = AppInternetConnection(context)

    private fun checkConnection(): Boolean {
        return FireBaseDataSource.IsConnected && internetTest.isConnected()
    }

    fun getGames(): MutableList<Game> {
        val games: MutableList<Game>
        if (checkConnection()) {
            games = dataSource.getGames()
            gameLocalDB.setGames(games)
        } else {
            games = gameLocalDB.getGames()
        }

        return games
    }

    fun createGame(genre: String, name: String, score: Int, description: String, uid: String, image: ByteArray): Task<String> {
        val key: String = (if (checkConnection()) dataSource.generateGameKey() else UUID.randomUUID()).toString()
        val game = Game(key, genre, name, score, description, uid, ModelStatus.CREATED.ordinal, gameImage = image)

        return if (checkConnection()) {
            dataSource.createGame(game)
        } else {
            gameLocalDB.createGame(game)
        }
    }

    fun updateGame(key: String, genre: String, name: String, score: Int, description: String, uid: String, image: ByteArray): Task<String> {
        val game = Game(key, genre, name, score, description, uid, ModelStatus.UPDATED.ordinal, gameImage = image)

        return if (checkConnection()) {
            dataSource.createGame(game)
        } else {
            gameLocalDB.updateGame(game)
        }
    }

    fun deleteGame(game: Game): Task<String> {
        return if (checkConnection()) {
            dataSource.deleteGame(game.id)
        } else {
            gameLocalDB.deleteGame(game)
        }
    }

    fun saveComment(gameId: String, text: String, uid: String): Task<String> {
        val key: String = (if (checkConnection()) dataSource.generateCommentKey(gameId) else UUID.randomUUID()).toString()
        val comment = Comment(key, gameId, text, uid)

        return if (checkConnection()) {
            dataSource.saveComment(comment)
        } else {
            commentLocalDB.createComment(comment)
        }
    }

    fun syncGames() {
        SyncManager.syncLocalDB(context = context, gameDataSource = dataSource)
    }
}