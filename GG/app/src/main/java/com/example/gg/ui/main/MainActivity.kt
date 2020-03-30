package com.example.gg.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.gg.R
import com.example.gg.data.model.Game
import com.example.gg.ui.newGame.CreateNewGame

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    var games: MutableList<Game>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainViewModel  = ViewModelProviders.of(this, MainViewModelFactory {
            games = mainViewModel.getGames()
            findViewById<ProgressBar>(R.id.gamesLoading).visibility = View.GONE
        }).get(MainViewModel::class.java)

        fab.setOnClickListener { view ->
            val intent = Intent(view.context, CreateNewGame::class.java)
            startActivity(intent)
            finish()
        }
    }

}
