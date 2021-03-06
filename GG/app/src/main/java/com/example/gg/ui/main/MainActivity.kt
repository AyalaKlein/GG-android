package com.example.gg.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.gg.R
import com.example.gg.R.drawable.ic_launcher_background
import com.example.gg.data.model.Comment
import com.example.gg.data.model.Game
import com.example.gg.data.sync.SyncManager
import com.example.gg.ui.gameDetails.GameDetails
import com.example.gg.ui.login.LoginActivity
import com.example.gg.ui.login.LoginViewModel
import com.example.gg.ui.login.LoginViewModelFactory
import com.example.gg.ui.newGame.CreateNewGame
import kotlinx.android.synthetic.main.activity_game.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.io.ByteArrayOutputStream


@ObsoleteCoroutinesApi
@InternalCoroutinesApi
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private var games: MutableList<Game>? = null

    private lateinit var loginViewModel: LoginViewModel
    var adapter: GameAdapter? = null
    var gamesList = arrayListOf<Game>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        @RequiresApi(Build.VERSION_CODES.P)
        fun afterLoad() {
            gamesList = arrayListOf()
            games?.forEach {
                gamesList.add(it)
            }

            gamesList.sortByDescending { it.id }
            adapter = GameAdapter(this, gamesList)

            gvGames.adapter = adapter
            findViewById<ProgressBar>(R.id.gamesLoad).visibility = View.GONE

            val searchView = findViewById<SearchView>(R.id.gSearch)
            val listener = object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    val filterGames = ArrayList<Game>()
                    gamesList.forEach {
                        if (it.name.toLowerCase().contains("$newText".toLowerCase())){
                            filterGames.add(it)
                        }
                    }
                    adapter!!.gameList = filterGames
                    gvGames.adapter = adapter
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

            }

            searchView.setOnQueryTextListener(listener)
        }

        mainViewModel  = ViewModelProviders.of(this, MainViewModelFactory(applicationContext) {
            games = mainViewModel.getGames()
            afterLoad()
        }).get(MainViewModel::class.java)

        games = mainViewModel.getGames()
        afterLoad()

        fab.setOnClickListener { view ->
            val intent = Intent(view.context, CreateNewGame::class.java)
            startActivity(intent)
            finish()
        }

        refresh.setOnClickListener {
            mainViewModel.syncGames()
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

    @InternalCoroutinesApi
    class GameAdapter : BaseAdapter {
        var gameList = ArrayList<Game>()
        var context: Context? = null

        constructor(context: Context, gameList: ArrayList<Game>) : super() {
            this.context = context
            this.gameList = gameList
        }

        override fun getCount(): Int {
            return gameList.size
        }

        override fun getItem(position: Int): Any {
            return gameList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        private fun getSelectedImageByteArray(image: ImageView): ByteArray {
            image.isDrawingCacheEnabled = true
            image.buildDrawingCache()
            val bitmap = (image.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            return baos.toByteArray()
        }

        private fun setSelectedImageByteArray(image: ImageView, bytes: ByteArray) {
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            image.setImageBitmap(
                Bitmap.createScaledBitmap(
                    bmp, image.width,
                    image.height, false
                )
            )
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val game = this.gameList[position]

            val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val gameView = inflator.inflate(R.layout.activity_game, null)
            val imgFood: ImageView = gameView.findViewById(R.id.imgFood)
            var listener: View.OnLayoutChangeListener? = null

            listener = View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                imgFood.removeOnLayoutChangeListener(listener!!)
                game.gameImage?.let { setSelectedImageByteArray(imgFood, it) }
            }
            imgFood.addOnLayoutChangeListener(listener)


            val commentsList = ArrayList<Comment>()
            game.Comments?.forEach {
                commentsList.add(it.value)
            }
            gameView.tvName.text = game.name
            gameView.setOnClickListener { v ->

                val intent = Intent(context,GameDetails::class.java)
                intent.putExtra("sGame", game)

                context!!.startActivity(intent)
                Toast.makeText(context, "You Clicked: " + v.tvName.text,Toast.LENGTH_SHORT).show()
            }
            return gameView
        }
    }
}
