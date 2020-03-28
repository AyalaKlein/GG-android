package com.example.gg.data.dataSource

import com.example.gg.data.model.Game
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameDataSource constructor(val callback: (Unit) -> Unit) {

    private var _db: DatabaseReference? = null
    private val _tableName: String = "Games"

    private var _games: MutableList<Game>

    init {
        _db  = Firebase.database.reference
        _games = mutableListOf()
    }

    private fun initGameList() {
        val gameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _games.clear()
                dataSnapshot.children.mapNotNullTo(_games) { it.getValue<Game>(Game::class.java) }
                callback(Unit)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        _db!!.child(_tableName).addListenerForSingleValueEvent(gameListener)
    }

    init {
        initGameList()
    }

    fun getGames(): MutableList<Game> {
        return this._games
    }
}