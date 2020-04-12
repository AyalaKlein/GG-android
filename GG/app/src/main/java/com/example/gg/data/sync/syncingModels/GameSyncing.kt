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
    fun syncGames(): Task<MutableList<Task<*>>>? {
        val gamesToSync = gameDao.getAll()
        val tasks = mutableListOf<Task<String>>()

        for (currGame in gamesToSync) {
            when(ModelStatus.fromInt(currGame.status)) {
                ModelStatus.CREATED, ModelStatus.UPDATED -> {
                    tasks.add(gameDataSource.createGame(currGame))
                }
                ModelStatus.DELETED -> {
                    tasks.add(gameDataSource.deleteGame(currGame.id))
                }
            }
        }

        return Tasks.whenAllComplete(tasks)
    }
}