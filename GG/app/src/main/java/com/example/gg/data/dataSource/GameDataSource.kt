package com.example.gg.data.dataSource

import android.content.Context
import android.net.Uri
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.model.Comment
import com.example.gg.data.model.Game
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage

class GameDataSource(val context: Context, val callback: ((Unit) -> Unit)?) {

    private var _db: DatabaseReference? = null
    private var _storage: StorageReference? = null
    private val _tableName: String = "Games"
    private var _isConnected: Boolean = false

    private var _games: MutableList<Game>

    init {
        _db = Firebase.database.reference
        _storage = Firebase.storage.reference
        _games = mutableListOf()
    }

    private fun initConnectionStatus() {
        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                _isConnected = snapshot.getValue(Boolean::class.java) ?: false
            }
        })
    }

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
        _db!!.child(_tableName).addListenerForSingleValueEvent(gameListener)
    }

    init {
        initConnectionStatus()
        initGameList()
    }

    fun getGames(): MutableList<Game> {
        return this._games
    }

    fun createGame(
        genre: String,
        name: String,
        score: Int,
        description: String,
        uid: String
    ): Task<String> {
        val key = _db!!.child("Games").push().key

        val game = key?.let { Game(it, genre, name, score, description, uid) }
        val gameValues = game?.toMap()

        val childUpdates = HashMap<String, Any>()
        if (!gameValues.isNullOrEmpty()) {
            childUpdates["/Games/$key"] = gameValues
        }

        return _db!!.updateChildren(childUpdates).continueWith(Continuation<Void, String> {
            return@Continuation key
        })
    }

    fun updateGame(
        key: String,
        genre: String,
        name: String,
        score: Int,
        description: String,
        uid: String
    ): Task<String> {
        val game = key?.let { Game(it, genre, name, score, description, uid) }
        val gameValues = game?.toMap()

        val childUpdates = HashMap<String, Any>()
        if (!gameValues.isNullOrEmpty()) {
            childUpdates["/Games/$key"] = gameValues
        }

        return _db!!.updateChildren(childUpdates).continueWith(Continuation<Void, String> {
            return@Continuation key
        })
    }

    fun saveComment(gameId: String, text: String, uid: String): Task<String> {
        val key = _db!!.child("Games/$gameId/Comments").push().key

        val comment = key?.let { Comment(it, gameId, text, uid) }
        val commentValues = comment?.toMap()

        val childUpdates = HashMap<String, Any>()
        if (!commentValues.isNullOrEmpty()) {
            childUpdates["/Games/$gameId/Comments/$key"] = commentValues
        }

        return _db!!.updateChildren(childUpdates).continueWith(Continuation<Void, String> {
            return@Continuation key
        })
    }

    fun saveImage(uid: String, data: ByteArray): UploadTask {
        val imageRef: StorageReference? = _storage!!.child("images/${uid}.jpg")
        return imageRef!!.putBytes(data)
    }

    fun getImageUrl(uid: String): Task<Uri> {
        val imageRef = _storage!!.child("images/${uid}.jpg")
        return imageRef?.downloadUrl
    }

    fun deleteGame(gameId: String): Task<Void> {
        return _db!!.child("$_tableName/$gameId").removeValue()
    }
}