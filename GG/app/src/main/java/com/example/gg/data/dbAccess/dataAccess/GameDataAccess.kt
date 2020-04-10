package com.example.gg.data.dbAccess.dataAccess

import android.content.Context
import com.example.gg.data.dataSource.FireBaseDataSource
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.model.Game
import com.example.gg.data.model.ModelStatus
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class GameDataAccess(val context: Context) {
    private var offlineGames: MutableList<Game> = mutableListOf<Game>()

    fun getGames(): MutableList<Game> {
        return this.offlineGames.filter { currGame -> currGame.status != ModelStatus.DELETED.ordinal }.toMutableList()
    }

    fun setGames(games: MutableList<Game>) {
        this.offlineGames = games
    }

    fun createGame(game: Game): Task<String> {
        AppDatabase.getDatabase(context).gameDao().insert(game)
        this.offlineGames.add(game)
//        callback?.let { it(Unit) }
        return Tasks.call {
            return@call game.id
        }
    }

    fun updateGame(game: Game): Task<String> {
        AppDatabase.getDatabase(context).gameDao().insert(game)

        for (index in 0 until this.offlineGames.size) {
            val currGame = this.offlineGames[index]
            if (currGame.id === game.id) {
                this.offlineGames[index] = currGame.copy(genre = game.genre, description = game.description, name = game.name, score = game.score)
                break
            }
        }
//
//        callback?.let { it(Unit) }
        return Tasks.call {
            return@call game.id
        }
    }

    fun deleteGame(key: String): Task<String> {
        for (index in 0 until this.offlineGames.size) {
            val currGame = this.offlineGames[index]
            if (currGame.id === key) {
                this.offlineGames[index].status = ModelStatus.DELETED.ordinal
                break
            }
        }

        return Tasks.call {
            return@call key
        }
    }
}