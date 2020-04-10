package com.example.gg.data.dbAccess.dataAccess

import android.content.Context
import com.example.gg.data.dataSource.FireBaseDataSource
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.model.Game
import com.example.gg.data.model.ModelStatus
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class GameDataAccess(val context: Context) {
    private var offlineGames: MutableList<Game> = mutableListOf<Game>()

    fun getGames(): MutableList<Game> {
        return this.offlineGames
    }

    fun setGames(games: MutableList<Game>) {
        this.offlineGames = games
    }

    fun createGame(game: Game): Task<String> {
        AppDatabase.getDatabase(context).gameDao().insert(game)
//        this._games.add(game)
//        callback?.let { it(Unit) }
        return Tasks.call {
            return@call game.id
        }
    }

    fun updateGame(game: Game): Task<String> {
        AppDatabase.getDatabase(context).gameDao().insert(game)

//        for (index in 0 until this._games.size) {
//            val currGame = this._games[index]
//            if (currGame.id === game.id) {
//                this._games[index] = currGame.copy(genre = game.genre, description = game.description, name = game.name, score = game.score)
//                break
//            }
//        }
//
//        callback?.let { it(Unit) }
        return Tasks.call {
            return@call game.id
        }
    }
}