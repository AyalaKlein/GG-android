package com.example.gg.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.gg.R
import com.example.gg.data.model.Game
import com.example.gg.ui.gameDetails.GameDetails
import com.example.gg.ui.newGame.CreateNewGame
import kotlinx.android.synthetic.main.activity_game.view.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private var games: MutableList<Game>? = null
    var adapter: GameAdapter? = null
    var gamesList = ArrayList<Game>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fun afterLoad() {
            games?.forEach {
                gamesList.add(it)
            }

            adapter = GameAdapter(this, gamesList, mainViewModel)

            gvGames.adapter = adapter
            findViewById<ProgressBar>(R.id.gamesLoading).visibility = View.GONE
        }

        mainViewModel  = ViewModelProviders.of(this, MainViewModelFactory {
            games = mainViewModel.getGames()
            afterLoad()
        }).get(MainViewModel::class.java)


        fab.setOnClickListener { view ->
            val intent = Intent(view.context, CreateNewGame::class.java)
            startActivity(intent)
            finish()
        }
    }

    class GameAdapter : BaseAdapter {
        var gameList = ArrayList<Game>()
        var context: Context? = null
        var imageRetriever: ImageRetriever? = null

        constructor(context: Context, gameList: ArrayList<Game>, imageRetriever: ImageRetriever) : super() {
            this.context = context
            this.gameList = gameList
            this.imageRetriever = imageRetriever
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

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val game = this.gameList[position]

            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var gameView = inflator.inflate(R.layout.activity_game, null)

            imageRetriever!!.getImageUrl(game.id).addOnCompleteListener {
                if (it.isSuccessful) {
                    Glide
                        .with(context!!)
                        .load(it.result!!)
                        .into(gameView.imgFood)
                } else {
                    gameView.imgFood.setImageResource(R.drawable.ic_launcher_background!!)
                }
            }

            gameView.tvName.text = game.name!!
            gameView.setOnClickListener { v ->
                val intent = Intent(context,GameDetails::class.java)
                intent.putExtra("sId", game.id)
                intent.putExtra("sName", game.name)
                intent.putExtra("sGenre", game.genre)
                intent.putExtra("sScore", game.score.toString())
                intent.putExtra("sDesc", game.description)
                context!!.startActivity(intent)
                Toast.makeText(context, "You Clicked: " + v.tvName.text,Toast.LENGTH_SHORT).show()
            }
            return gameView
        }
    }
}
