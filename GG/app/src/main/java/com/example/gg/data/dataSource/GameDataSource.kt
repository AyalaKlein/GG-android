package com.example.gg.data.dataSource

import com.example.gg.data.model.Comment
import com.example.gg.data.model.Game
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class GameDataSource(val callback: ((Unit) -> Unit)?) {

    private val _tableName: String = "Games"
    private var _games: MutableList<Game> = mutableListOf<Game>()

    private val gameListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            _games.clear()
            dataSnapshot.children.mapNotNullTo(_games) {
                it.getValue<Game>(Game::class.java)
            }

            val tasks = mutableListOf<Task<ByteArray?>>()
            _games.forEach {game ->
                tasks.add(getImageUrl(game.id).addOnCompleteListener {
                    if (it.isSuccessful)
                        game.gameImage = it.result
                    else
                        game.gameImage = byteArrayOf()
                })
            }

            Tasks.whenAllComplete(tasks).addOnCompleteListener {
                callback?.let { it(Unit) }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("loadPost:onCancelled ${databaseError.toException()}")
        }
    }

    fun initGameList() {
        FireBaseDataSource.DbRef.child(_tableName).addListenerForSingleValueEvent(gameListener)
    }

    fun disableGameList() {
        FireBaseDataSource.DbRef.child(_tableName).removeEventListener(gameListener)
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
            return@Continuation saveImage(game.id, game.gameImage!!).result
        })
    }

    fun generateGameKey(): String {
        return FireBaseDataSource.DbRef.child(_tableName).push().key.toString()
    }

    fun generateCommentKey(gameId: String): String {
        return FireBaseDataSource.DbRef.child("Games/$gameId/Comments").push().key.toString()
    }

    fun saveComment(comment: Comment): Task<String> {
        val commentValues = comment.toMap()
        val childUpdates = HashMap<String, Any>()
        if (!commentValues.isNullOrEmpty()) {
            childUpdates["/Games/${comment.gameId}/Comments/${comment.id}"] = commentValues
        }

        return FireBaseDataSource.DbRef.updateChildren(childUpdates).continueWith(Continuation<Void, String> {
            return@Continuation comment.id
        })
    }

    private fun saveImage(uid: String, data: ByteArray): Task<String> {
        val imageRef: StorageReference? = FireBaseDataSource.StorageRef.child("images/${uid}.jpg")
        return imageRef!!.putBytes(data).continueWith(Continuation {
            return@Continuation uid
        })
    }

    private fun getImageUrl(uid: String): Task<ByteArray?> {
        val imageRef = FireBaseDataSource.StorageRef.child("images/${uid}.jpg")
        return imageRef.getBytes(1024 * 1024 * 100)
    }

    fun deleteGame(gameId: String): Task<String> {
        return FireBaseDataSource.DbRef.child("$_tableName/$gameId").removeValue().continueWith(Continuation<Void, String> {
            return@Continuation gameId
        })
    }
}