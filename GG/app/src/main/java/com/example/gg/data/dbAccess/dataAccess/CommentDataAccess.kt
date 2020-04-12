package com.example.gg.data.dbAccess.dataAccess

import android.content.Context
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.model.Comment
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class CommentDataAccess(val context: Context) {
    private var commentDao = AppDatabase.getDatabase(context).commentDao()

    fun createComment(comment: Comment): Task<String> {
        commentDao.insert(comment)
        return Tasks.call {
            return@call comment.id
        }
    }
}