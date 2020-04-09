package com.example.gg.data.dataSource

import android.content.Context
import android.net.Uri
import com.example.gg.data.dbAccess.AppDatabase
import com.example.gg.data.dbAccess.dao.CommentDao
import com.example.gg.data.dbAccess.dao.GameDao
import com.example.gg.data.model.Comment
import com.example.gg.data.model.Game
import com.example.gg.data.model.Image
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import java.io.ByteArrayInputStream
import java.security.Key
import java.util.*
import kotlin.collections.HashMap

@InternalCoroutinesApi
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
        val key: String = (if (_isConnected) _db!!.child("Games").push().key else UUID.randomUUID()).toString()

        val game = Game(key, genre, name, score, description, uid)

        if (_isConnected) {
            val gameValues = game.toMap()

            val childUpdates = HashMap<String, Any>()
            if (!gameValues.isNullOrEmpty()) {
                childUpdates["/Games/$key"] = gameValues
            }

            return _db!!.updateChildren(childUpdates).continueWith(Continuation<Void, String> {
                return@Continuation key
            })
        } else {
            AppDatabase.getDatabase(context).gameDao().insert(game)
            this._games.add(game)
            callback?.let { it(Unit) }
            return Tasks.call {
                return@call key
            }
        }
    }

    fun updateGame(
        key: String,
        genre: String,
        name: String,
        score: Int,
        description: String,
        uid: String
    ): Task<String> {
        val game = Game(key, genre, name, score, description, uid)

        if (_isConnected) {
            val gameValues = game.toMap()

            val childUpdates = HashMap<String, Any>()
            if (!gameValues.isNullOrEmpty()) {
                childUpdates["/Games/$key"] = gameValues
            }

            return _db!!.updateChildren(childUpdates).continueWith(Continuation<Void, String> {
                return@Continuation key
            })
        } else {
            AppDatabase.getDatabase(context).gameDao().insert(game)

            for (index in 0 until this._games.size) {
                val currGame = this._games[index]
                if (currGame.id === game.id) {
                    this._games[index] = currGame.copy(genre = game.genre, description = game.description, name = game.name, score = game.score)
                    break
                }
            }

            callback?.let { it(Unit) }
            return Tasks.call {
                return@call key
            }
        }
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

    fun saveImage(uid: String, data: ByteArray): Task<String> {
        if (_isConnected) {
            val imageRef: StorageReference? = _storage!!.child("images/${uid}.jpg")
            return imageRef!!.putBytes(data).continueWith(Continuation {
                return@Continuation uid
            })
        } else {
            val image = Image(uid, data.toString())
            AppDatabase.getDatabase(context).imageDao().insert(image)
            callback?.let { it(Unit) }
            return Tasks.call {
                return@call uid
            }
        }
    }

    fun getImageUrl(uid: String): Task<ByteArray?> {
        if (_isConnected) {
            val imageRef = _storage!!.child("images/${uid}.jpg")
            return imageRef.getBytes(1024 * 1024 * 100)
        } else {
            val image = AppDatabase.getDatabase(context).imageDao().get(uid)
            return Tasks.call {
                return@call image?.data?.toByteArray()
            }
        }
    }

    fun deleteGame(gameId: String): Task<Void> {
        return _db!!.child("$_tableName/$gameId").removeValue()
    }

    fun syncLocalDB(): Task<Void>? {
        val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
        scope.launch {
            val localDB = AppDatabase.getDatabase(context)
            val gameDao: GameDao = localDB.gameDao()
            val commentDao: CommentDao = localDB.commentDao()

            val updatedGames: List<Game> = gameDao.getAll()
            val updatedComments: List<Comment> = commentDao.getAll()


        }

        return null
    }
}