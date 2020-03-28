package com.example.gg.data.dataSource

import com.example.gg.data.model.Game
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameDataSource constructor() {

    private var _db: DatabaseReference? = null
    private val _tableName: String = "Games"

    private var _games: MutableList<Game>

    private fun initGameList() {
        val gameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _games.clear()
                dataSnapshot.children.mapNotNullTo(_games) { it.getValue<Game>(Game::class.java) }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        _db!!.child(_tableName).addListenerForSingleValueEvent(gameListener)
    }

    init {
        _db  = Firebase.database.reference
        _games = mutableListOf()
        initGameList()
    }

    fun getGames(): MutableList<Game> {
        return this._games
    }
}