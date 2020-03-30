package com.example.gg.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.gg.R
import com.example.gg.data.model.Game
import com.example.gg.ui.newGame.CreateNewGame
import kotlinx.android.synthetic.main.activity_game.view.*

import kotlinx.android.synthetic.main.activity_main.*

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

            adapter = GameAdapter(this, gamesList)

            gvGames.adapter = adapter
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

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val game = this.gameList[position]

            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var gameView = inflator.inflate(R.layout.activity_game, null)
            gameView.imgFood.setImageResource(R.drawable.common_google_signin_btn_icon_dark_focused!!)
            gameView.gName.text = game.name!!

            return gameView
        }
    }

}
