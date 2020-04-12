package com.example.gg.data.sync.syncingModels

import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.dbAccess.dao.GameDao
import com.example.gg.data.model.ModelStatus
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class GameSyncing(private val localDB: AppDatabase, private val gameDataSource: GameDataSource) {

    private val gameDao: GameDao = localDB.gameDao()
    fun syncGames(): Task<HashMap<String, String>> {
        val gamesToSync = gameDao.getAll()

        val idMapping = HashMap<String, String>()

        for (currGame in gamesToSync) {
            when(ModelStatus.valueOf(currGame.status.toString())) {
                ModelStatus.CREATED -> {
                    val newKey = gameDataSource.generateGameKey()
                    idMapping[currGame.id] = newKey
                    currGame.id = newKey

                    gameDataSource.createGame(currGame)
                }
                ModelStatus.UPDATED -> {
                    idMapping[currGame.id] = currGame.id

                    gameDataSource.updateGame(currGame)
                }
                ModelStatus.DELETED -> {
                    gameDataSource.deleteGame(currGame.id)
                }
            }
        }

        return Tasks.call {
            return@call idMapping
        }
    }
}