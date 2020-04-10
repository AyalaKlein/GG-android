package com.example.gg.data.dbAccess.dataAccess

import android.content.Context
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.model.Image
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ImageDataAccess(val context: Context) {
    fun saveImage(uid: String, data: ByteArray): Task<String> {
        val image = Image(uid, data.toString())
        AppDatabase.getDatabase(context).imageDao().insert(image)
//        callback?.let { it(Unit) }
        return Tasks.call {
            return@call uid
        }
    }

    fun getImageUrl(uid: String): Task<ByteArray?> {
        val image = AppDatabase.getDatabase(context).imageDao().get(uid)
        return Tasks.call {
            return@call image?.data?.toByteArray()
        }
    }
}