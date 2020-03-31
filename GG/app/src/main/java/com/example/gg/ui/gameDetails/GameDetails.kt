package com.example.gg.ui.gameDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.gg.R
import com.example.gg.ui.main.ImageRetriever
import com.example.gg.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_game.view.*
import kotlinx.android.synthetic.main.activity_game_details.*

class GameDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)
        val intent1 = Intent(this, MainActivity::class.java)
        parentActivityIntent?.getStringExtra("sId")
        var imageRetriever: ImageRetriever? = null



        gdNameV.text = intent.getStringExtra("sName")
        gdGenreV.text = intent.getStringExtra("sGenre")
        gdScoreV.text = intent.getStringExtra("sScore")
        gdDescV.text = intent.getStringExtra("sDesc")
    }
}
