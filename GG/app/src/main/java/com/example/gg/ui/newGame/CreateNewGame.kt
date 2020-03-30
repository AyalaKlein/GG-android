package com.example.gg.ui.newGame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.gg.R
import com.example.gg.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_create_new_game.*

class CreateNewGame : AppCompatActivity() {

    private lateinit var newGameViewModel: NewGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_game)

        newGameViewModel  = ViewModelProviders.of(this, NewGameViewModelFactory()).get(NewGameViewModel::class.java)

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
        newGameViewModel.createGame(genre, name, score, description).addOnCompleteListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
