package com.example.gg.data.dbAccess.dataAccess

import android.content.Context
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.model.Comment
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class CommentDataAccess(val context: Context) {

    fun saveComment(gameId: String, text: String, uid: String): Task<String> {
        val key: String = UUID.randomUUID().toString()
        val comment = Comment(key, gameId, text, uid)
        AppDatabase.getDatabase(context).commentDao().insert(comment)

//        for (index in 0 until this._games.size) {
//            val currGame = this._games[index]
//            if (currGame.id === comment.gameId) {
//                this._games[index].Comments!![key] = comment
//                break
//            }
//        }

        return Tasks.call {
            return@call key
        }
    }
}