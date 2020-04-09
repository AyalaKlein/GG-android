package com.example.gg.data.dbAccess

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gg.data.dbAccess.dao.CommentDao
import com.example.gg.data.dbAccess.dao.GameDao
import com.example.gg.data.model.Comment
import com.example.gg.data.model.Game
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Game::class, Comment::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun commentDao(): CommentDao

    companion object {
        private var _db: AppDatabase? = null
        @InternalCoroutinesApi
        fun GetDatabase(context: Context) : AppDatabase {
//        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
//        scope.launch {
//
//        }

            if (_db == null) {
                synchronized(AppDatabase::class) {
                    if (_db == null) {
                        _db = Room.databaseBuilder(context, AppDatabase::class.java, "GG-DB").build()
                    }
                }
            }

            return _db!!
        }
    }
}