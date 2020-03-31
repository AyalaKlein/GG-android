package com.example.gg.ui.gameDetails

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.gg.R
import com.example.gg.data.model.Comment
import com.example.gg.ui.editGame.EditGame
import com.example.gg.ui.main.ImageRetriever
import com.example.gg.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_comment.view.*
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
        val sId = intent.getStringExtra("sId")


        bEdit.setOnClickListener { view ->
            val intent = Intent(view.context, EditGame::class.java)
            intent.putExtra("sId", sId)
            intent.putExtra("sName", gdNameV.text.toString())
            intent.putExtra("sGenre", gdGenreV.text.toString())
            intent.putExtra("sScore", gdScoreV.text.toString())
            intent.putExtra("sDesc", gdDescV.text.toString())
            startActivity(intent)
            finish()
        }

    }

    class CommentAdapter : BaseAdapter {
        var commList = ArrayList<Comment>()
        var context: Context? = null

        constructor(context: Context, gameList: ArrayList<Comment>, imageRetriever: ImageRetriever) : super() {
            this.context = context
            this.commList = gameList
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
            return commentView
        }
    }

}
