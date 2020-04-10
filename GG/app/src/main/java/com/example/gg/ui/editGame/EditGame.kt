package com.example.gg.ui.editGame

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import com.example.gg.R
import com.example.gg.data.dataSource.FireBaseDataSource
import com.example.gg.ui.main.MainActivity
import com.example.gg.ui.newGame.NewGameViewModel
import com.example.gg.ui.newGame.NewGameViewModelFactory
import kotlinx.android.synthetic.main.activity_edit_game.*
import kotlinx.coroutines.InternalCoroutinesApi
import java.io.ByteArrayOutputStream

@InternalCoroutinesApi
@Suppress("DEPRECATION")
class EditGame : AppCompatActivity() {

    private lateinit var newGameViewModel: NewGameViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_game)

        game_name.editText?.setText(intent.getStringExtra("sName"))
        game_genre.editText?.setText(intent.getStringExtra("sGenre"))
        game_score.editText?.setText(intent.getStringExtra("sScore"))
        game_desc.editText?.setText(intent.getStringExtra("sDesc"))
        val gameId: String = intent.getStringExtra("sId")

        val byteArray: ByteArray = intent.getByteArrayExtra("sImage")!!
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val image =
            findViewById<ImageView>(R.id.game_image)

        image.setImageBitmap(
            bmp
        )
        newGameViewModel  = ViewModelProviders.of(this, NewGameViewModelFactory(applicationContext)).get(
            NewGameViewModel::class.java)

        update_game.setOnClickListener {
            updateGame(intent.getStringExtra("sId"),game_genre.editText?.text.toString(), game_name.editText?.text.toString(), game_score.editText?.text.toString().toInt(), game_desc.editText?.text.toString())
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

    private fun updateGame(key: String, genre: String, name: String, score: Int, description: String) {
        newGameViewModel.updateGame(key, genre, name, score, description, FireBaseDataSource.Auth.currentUser!!.uid).addOnCompleteListener {
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
