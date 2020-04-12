package com.example.gg.data.dbAccess.dataAccess

import android.content.Context
import android.content.ContextWrapper
import com.example.gg.data.dataSource.FireBaseDataSource
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.model.Comment
import com.example.gg.data.model.Game
import com.example.gg.data.model.ModelStatus
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.InternalCoroutinesApi
import java.io.File

@InternalCoroutinesApi
class GameDataAccess(val context: Context) {
    private var gameDao = AppDatabase.getDatabase(context).gameDao()

    fun getGames(): MutableList<Game> {
        val games = gameDao.getDisplayGames()
        val wrapper = ContextWrapper(context)
        val dir = wrapper.getDir("images", Context.MODE_PRIVATE)

        for (currGame in games) {
            currGame.Comments = HashMap()
            currGame.listComments!!.forEach {
                currGame.Comments!![it.id] = it
            }

            val imageFile = File(dir,"${currGame.id}.jpg")
            currGame.gameImage = imageFile.readBytes()
        }

        return games.toMutableList()
    }

    fun setGames(games: MutableList<Game>) {
        gameDao.deleteAll()

        val wrapper = ContextWrapper(context)
        val dir = wrapper.getDir("images", Context.MODE_PRIVATE)

        for (currGame in games) {
            currGame.status = ModelStatus.NO_CHANGE.ordinal
            currGame.listComments = currGame.Comments!!.values.toList()

            val imageFile = File(dir,"${currGame.id}.jpg")
            imageFile.writeBytes(currGame.gameImage!!)
        }

        gameDao.insert(games)
    }

    fun createGame(game: Game): Task<String> {
        game.status = ModelStatus.CREATED.ordinal

        val wrapper = ContextWrapper(context)
        val dir = wrapper.getDir("images", Context.MODE_PRIVATE)
        val imageFile = File(dir,"${game.id}.jpg")
        imageFile.writeBytes(game.gameImage!!)

        gameDao.insert(game)

        return Tasks.call {
            return@call game.id
        }
    }

    fun updateGame(game: Game): Task<String> {
        if (game.status == ModelStatus.NO_CHANGE.ordinal)
            game.status = ModelStatus.UPDATED.ordinal

        val wrapper = ContextWrapper(context)
        val dir = wrapper.getDir("images", Context.MODE_PRIVATE)
        val imageFile = File(dir,"${game.id}.jpg")
        imageFile.writeBytes(game.gameImage!!)

        gameDao.insert(game)

        return Tasks.call {
            return@call game.id
        }
    }

    fun deleteGame(game: Game): Task<String> {
        val wrapper = ContextWrapper(context)
        val dir = wrapper.getDir("images", Context.MODE_PRIVATE)
        val imageFile = File(dir,"${game.id}.jpg")
        imageFile.delete()

        if (game.status == ModelStatus.CREATED.ordinal) {
            gameDao.delete(game)
        } else {
            game.status = ModelStatus.DELETED.ordinal
            gameDao.insert(game)
        }

        return Tasks.call {
            return@call game.id
        }
    }
}