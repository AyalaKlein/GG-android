package com.example.gg.ui.newGame

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.gg.R
import com.example.gg.ui.login.afterTextChanged
import com.example.gg.ui.main.MainActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_new_game.*
import java.io.ByteArrayOutputStream

class CreateNewGame : AppCompatActivity() {

    private lateinit var newGameViewModel: NewGameViewModel
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var gameName: TextInputLayout
    private lateinit var gameImage: ImageView
    private lateinit var gameGenre: TextInputLayout
    private lateinit var gameScore: TextInputLayout
    private lateinit var gameDesc: TextInputLayout
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_game)

        newGameViewModel  = ViewModelProviders.of(this, NewGameViewModelFactory(applicationContext)).get(NewGameViewModel::class.java)

        newGameViewModel.createFormState.observe(this@CreateNewGame, Observer {
            saveButton.isEnabled = it
        })

        save_game.setOnClickListener {
            writeNewGame(game_genre.editText?.text.toString(), game_name.editText?.text.toString(), game_score.editText?.text.toString().toInt(), game_desc.editText?.text.toString())
        }

        img_pick_btn.setOnClickListener {
            pickImageFromGallery();
        }

        gameName = findViewById<TextInputLayout>(R.id.game_name)
        gameImage = findViewById<ImageView>(R.id.game_image)
        gameGenre = findViewById<TextInputLayout>(R.id.game_genre)
        gameScore = findViewById<TextInputLayout>(R.id.game_score)
        gameDesc = findViewById<TextInputLayout>(R.id.game_desc)
        saveButton = findViewById<Button>(R.id.save_game)

        gameName.editText!!.afterTextChanged {
            checkFormValid()
        }

        gameGenre.editText!!.afterTextChanged {
            checkFormValid()
        }

        gameScore.editText!!.afterTextChanged {
            checkFormValid()
        }

        gameDesc.editText!!.afterTextChanged {
            checkFormValid()
        }
    }

    private fun checkFormValid(): Boolean {
        return newGameViewModel.createGameDataChanged(gameName = gameName.editText?.text.toString(),
            gameImage = gameImage.drawable.current,
            gameGenre = gameGenre.editText?.text.toString(),
            gameScore = gameScore.editText?.text.toString(),
            gameDesc = gameDesc.editText?.text.toString())
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

        if (data != null) {
            checkFormValid()
        }
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
        if (checkFormValid()) {
            newGameViewModel.createGame(genre, name, score, description, auth.currentUser!!.uid)
                .addOnCompleteListener {
                    newGameViewModel.saveImage(it.result!!, getSelectedImageByteArray())
                        .addOnSuccessListener {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                }.addOnFailureListener {
                // error not good
            }
        }
    }
}