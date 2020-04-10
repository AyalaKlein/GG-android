package com.example.gg.data.sync.syncingModels

import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.dbAccess.dao.GameDao
import com.example.gg.data.model.ModelStatus
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

class GameSyncing(private val localDB: AppDatabase) {

    private val gameDao: GameDao = localDB.gameDao()
    fun syncGames(): Task<HashMap<String, String>> {
        val gamesToSync = gameDao.getAll()

        val idMapping = HashMap<String, String>()

        for (currGame in gamesToSync) {
            when(ModelStatus.valueOf(currGame.status.toString())) {
                ModelStatus.CREATED -> {

                }
                ModelStatus.UPDATED -> {

                }
                ModelStatus.DELETED -> {

                }
            }
        }

        return Tasks.call {
            return@call idMapping
        }
    }
}