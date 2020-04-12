package com.example.gg.data.sync

import android.content.Context
import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.sync.syncingModels.GameSyncing
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*

abstract class SyncManager {
    @ObsoleteCoroutinesApi
    @InternalCoroutinesApi
    companion object {
        fun syncLocalDB(context: Context, gameDataSource: GameDataSource): Task<Void>? {
            gameDataSource.disableGameList()
            val scope = CoroutineScope(newFixedThreadPoolContext(1, "synchronizationPool"))
            scope.launch {
                val localDB = AppDatabase.getDatabase(context)
//                val gameDao: GameDao = localDB.gameDao()
//                val commentDao: CommentDao = localDB.commentDao()
//                val imageDao: ImageDao = localDB.imageDao()

                val ids = GameSyncing(localDB, gameDataSource).syncGames()
                gameDataSource.initGameList()
            }

            return null
        }
    }
}