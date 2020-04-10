package com.example.gg.data.sync

import com.google.android.gms.tasks.Task

abstract class SyncManager {
    companion object {
        fun syncLocalDB(): Task<Void>? {
//            val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
//            scope.launch {
//                val localDB = AppDatabase.getDatabase(context)
//                val gameDao: GameDao = localDB.gameDao()
//                val commentDao: CommentDao = localDB.commentDao()
//                val imageDao: ImageDao = localDB.imageDao()
//
//                val updatedGames: List<Game> = gameDao.getAll()
//                val updatedComments: List<Comment> = commentDao.getAll()
//                val updatedImages: List<Image>? = null
//            }

            return null
        }
    }
}