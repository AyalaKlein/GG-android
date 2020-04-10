package com.example.gg.data.dataSource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

abstract class FireBaseDataSource {
    companion object {
        private var _baseDB: FirebaseDatabase
        private lateinit var _db: DatabaseReference
        private lateinit var _storage: StorageReference
        private var _isConnected: Boolean = false
        private lateinit var _auth: FirebaseAuth

        var DbRef: DatabaseReference
            get() {
                return _db
            }
            private set(value) {
                this._db = value
            }

        var StorageRef: StorageReference
            get() {
                return _storage
            }
            private set(value) {
                this._storage = value
            }

        var IsConnected: Boolean
            get() {
                return _isConnected
            }
            private set(value) {
                this._isConnected = value
            }

        var Auth: FirebaseAuth
            get() {
                return _auth
            }
            private set(value) {
                this._auth = value
            }

        init {
            Firebase.database.setPersistenceEnabled(false)
            Firebase.database.reference.keepSynced(false)

            _baseDB = Firebase.database
            DbRef = _baseDB.reference
            StorageRef = Firebase.storage.reference

            Auth = FirebaseAuth.getInstance()

            initConnectionStatus()
        }

        private fun initConnectionStatus() {
            val connectedRef = _baseDB.getReference(".info/connected")
            connectedRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    _isConnected = snapshot.getValue(Boolean::class.java) ?: false
                }
            })
        }
    }
}