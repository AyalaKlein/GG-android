package com.example.gg.ui.gameDetails

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProviders
import com.example.gg.CommentPopUpActivity
import com.example.gg.R
import com.example.gg.data.dataSource.FireBaseDataSource
import com.example.gg.data.model.Comment
import com.example.gg.data.model.Game
import com.example.gg.ui.editGame.EditGame
import com.example.gg.ui.login.LoginActivity
import com.example.gg.ui.login.LoginViewModel
import com.example.gg.ui.login.LoginViewModelFactory
import com.example.gg.ui.main.MainActivity
import com.example.gg.ui.newGame.NewGameViewModel
import com.example.gg.ui.newGame.NewGameViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_comment.view.*
import kotlinx.android.synthetic.main.activity_game_details.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.io.ByteArrayOutputStream


@ObsoleteCoroutinesApi
@InternalCoroutinesApi
@Suppress("DEPRECATION")
class GameDetails : AppCompatActivity() {

    private lateinit var newGameViewModel: NewGameViewModel

    private lateinit var loginViewModel: LoginViewModel
    private val commentsList = ArrayList<Comment>()
    private var adapter: CommentAdapter? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)
        val intent1 = Intent(this, MainActivity::class.java)

        newGameViewModel  = ViewModelProviders.of(this, NewGameViewModelFactory(applicationContext)).get(
            NewGameViewModel::class.java)

        var sGame = Game()
        sGame = intent.getSerializableExtra("sGame") as Game
        gdGenreV.text =  sGame.name
        gdScoreV.text =  sGame.score.toString()
        gdDescV.text =  sGame.description
        gdNameV.text = sGame.name

        val comments: HashMap<String, Comment> = sGame.Comments as HashMap<String, Comment>
        comments.forEach {
            commentsList.add(it.value)
        }

        val newComment = Comment()
        if(!intent.getStringExtra("newComm").isNullOrEmpty()) {
            commentsList.add(Comment("","", intent.getStringExtra("newComm")))
        }
        adapter = CommentAdapter(this, commentsList)

        gvComm.adapter = adapter

        val sId = sGame.id
        var userId = sGame.userId
        val byteArray: ByteArray = sGame.gameImage!!
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val image =
            findViewById<ImageView>(R.id.gdImage)

        image.setImageBitmap(
            bmp
        )

        if (FireBaseDataSource.Auth.currentUser!!.uid == userId) {
            bEdit.visibility = View.VISIBLE
            bEdit.setOnClickListener { view ->
                val intent = Intent(view.context, EditGame::class.java)
                intent.putExtra("sId", sId)
                intent.putExtra("sName", gdNameV.text.toString())
                intent.putExtra("sGenre", gdGenreV.text.toString())
                intent.putExtra("sScore", gdScoreV.text.toString())
                intent.putExtra("sDesc", gdDescV.text.toString())
                intent.putExtra("sImage", getSelectedImageByteArray(gdImage))
                startActivity(intent)
                finish()
            }


            bDelete.visibility = View.VISIBLE
            bDelete.setOnClickListener {
                deleteGame(sGame)
            }

        }

        bComm.setOnClickListener {
            val intent = Intent(this, CommentPopUpActivity::class.java)
            intent.putExtra("sGame", sGame)
            intent.putExtra("sImage", getSelectedImageByteArray(gdImage))
            intent.putExtra("gameId", sId)
            intent.putExtra("popupbtn", "OK")
            intent.putExtra("darkstatusbar", false)
            startActivity(intent)
        }

        val callback = this.onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        }

    private fun getSelectedImageByteArray(image: ImageView): ByteArray {
        image.isDrawingCacheEnabled = true
        image.buildDrawingCache()
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        return baos.toByteArray()
    }
    
    private fun deleteGame(game: Game) {
        newGameViewModel.deleteGame(game).addOnCompleteListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "Your Game Removed Successfully",Toast.LENGTH_SHORT).show()
                finish()
        }.addOnFailureListener {
            // error not good
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_favorite -> {
            loginViewModel  = ViewModelProviders.of(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)
            loginViewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.
            FLAG_ACTIVITY_CLEAR_TASK.
            or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            true

        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}

    class CommentAdapter : BaseAdapter {
        var commList = ArrayList<Comment>()
        var context: Context? = null

        constructor(context: Context, commList: ArrayList<Comment>) : super() {
            this.context = context
            this.commList = commList
        }

        override fun getCount(): Int {
            return commList.size
        }

        override fun getItem(position: Int): Any {
            return commList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val comment = this.commList[position]

            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var commentView = inflater.inflate(R.layout.activity_comment, null)

            commentView.gComment.text = comment.text!!
            commentView.userName.text = comment.user
            return commentView
        }

    }