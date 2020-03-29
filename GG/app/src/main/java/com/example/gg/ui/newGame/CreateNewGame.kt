package com.example.gg.ui.newGame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gg.R
import com.example.gg.data.model.Game
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_new_game.*

class CreateNewGame : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_game)
        database = Firebase.database.reference

        save_game.setOnClickListener {

            val aa = game_genre.editText
            writeNewGame(game_genre.editText?.text.toString(), game_name.editText?.text.toString(), game_score.editText?.text.toString().toInt(), game_desc.editText?.text.toString())
        }

        img_pick_btn.setOnClickListener {
                    pickImageFromGallery();
                }
            }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
        game_image.setImageURI(intent?.data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        game_image.setImageURI(data?.data)
    }

    private fun writeNewGame(genre: String, name: String, score: Int, description: String) {
        val key = database.child("Games").push().key

        val game = key?.let { Game(it, genre, name, score, description) }
        val gameValues = game?.toMap()

        val childUpdates = HashMap<String, Any>()
        if(!gameValues.isNullOrEmpty()) {
            childUpdates["/Games/$key"] = gameValues
        }

        database.updateChildren(childUpdates)
    }
}
