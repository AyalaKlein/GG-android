package com.example.gg.data.dataSource

import android.content.Context
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.dbAccess.dao.CommentDao
import com.example.gg.data.dbAccess.dao.GameDao
import com.example.gg.data.dbAccess.dao.ImageDao
import com.example.gg.data.model.Comment
import com.example.gg.data.model.Game
import com.example.gg.data.model.Image
import com.example.gg.data.model.ModelStatus
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import java.util.*
import kotlin.collections.HashMap

@InternalCoroutinesApi
class GameDataSource(val callback: ((Unit) -> Unit)?) {

    private val _tableName: String = "Games"
    private var _games: MutableList<Game> = mutableListOf<Game>()

    private fun initGameList() {
        val gameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _games.clear()
                dataSnapshot.children.mapNotNullTo(_games) {
                    it.getValue<Game>(Game::class.java)
                }
                callback?.let { it(Unit) }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FireBaseDataSource.DbRef.child(_tableName).addListenerForSingleValueEvent(gameListener)
    }

    init {
        initGameList()
    }

    fun getGames(): MutableList<Game> {
        return this._games
    }

    fun createGame(game: Game): Task<String> {
        val gameValues = game.toMap()

        val childUpdates = HashMap<String, Any>()
        if (!gameValues.isNullOrEmpty()) {
            childUpdates["/Games/${game.id}"] = gameValues
        }

        return FireBaseDataSource.DbRef.updateChildren(childUpdates).continueWith(Continuation<Void, String> {
            return@Continuation game.id
        })
    }

    fun updateGame(game: Game): Task<String> {
        val gameValues = game.toMap()

        val childUpdates = HashMap<String, Any>()
        if (!gameValues.isNullOrEmpty()) {
            childUpdates["/Games/${game.id}"] = gameValues
        }

        return FireBaseDataSource.DbRef.updateChildren(childUpdates).continueWith(Continuation<Void, String> {
            return@Continuation game.id
        })
    }

    fun saveComment(gameId: String, text: String, uid: String): Task<String> {
        val key: String = FireBaseDataSource.DbRef.child("Games/$gameId/Comments").push().key.toString()
        val comment = Comment(key, gameId, text, uid)

        val commentValues = comment.toMap()
        val childUpdates = HashMap<String, Any>()
        if (!commentValues.isNullOrEmpty()) {
            childUpdates["/Games/$gameId/Comments/$key"] = commentValues
        }

        return FireBaseDataSource.DbRef.updateChildren(childUpdates).continueWith(Continuation<Void, String> {
            return@Continuation key
        })
    }

    fun saveImage(uid: String, data: ByteArray): Task<String> {
        val imageRef: StorageReference? = FireBaseDataSource.StorageRef.child("images/${uid}.jpg")
        return imageRef!!.putBytes(data).continueWith(Continuation {
            return@Continuation uid
        })
    }

    fun getImageUrl(uid: String): Task<ByteArray?> {
        val imageRef = FireBaseDataSource.StorageRef.child("images/${uid}.jpg")
        return imageRef.getBytes(1024 * 1024 * 100)
    }

    fun deleteGame(gameId: String): Task<String> {
        return FireBaseDataSource.DbRef.child("$_tableName/$gameId").removeValue().continueWith(Continuation<Void, String> {
            return@Continuation gameId
        })
    }
}