package com.example.gg.ui.newGame

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.gg.R
import com.example.gg.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_new_game.*
import java.io.ByteArrayOutputStream

class CreateNewGame : AppCompatActivity() {

    private lateinit var newGameViewModel: NewGameViewModel
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

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

    private fun getSelectedImageByteArray(): ByteArray {
        game_image.isDrawingCacheEnabled = true
        game_image.buildDrawingCache()
        val bitmap = (game_image.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        return baos.toByteArray()
    }

    private fun writeNewGame(genre: String, name: String, score: Int, description: String) {
        newGameViewModel.createGame(genre, name, score, description, auth.currentUser!!.uid).addOnCompleteListener {
            newGameViewModel.saveImage(it.result!!, getSelectedImageByteArray()).addOnSuccessListener {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            // error not good
        }
    }
}
