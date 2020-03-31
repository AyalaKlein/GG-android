package com.example.gg.ui.gameDetails

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.gg.R
import com.example.gg.data.model.Comment
import com.example.gg.ui.editGame.EditGame
import com.example.gg.ui.main.ImageRetriever
import com.example.gg.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_game_details.*
import java.io.ByteArrayOutputStream


class GameDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)
        val intent1 = Intent(this, MainActivity::class.java)


        gdNameV.text = intent.getStringExtra("sName")
        gdGenreV.text = intent.getStringExtra("sGenre")
        gdScoreV.text = intent.getStringExtra("sScore")
        gdDescV.text = intent.getStringExtra("sDesc")
        val sId = intent.getStringExtra("sId")
        val byteArray: ByteArray = intent.getByteArrayExtra("sImage")!!
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val image =
            findViewById<ImageView>(R.id.gdImage)

        image.setImageBitmap(
            bmp
        )

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

    }

    private fun getSelectedImageByteArray(image: ImageView): ByteArray {
        image.isDrawingCacheEnabled = true
        image.buildDrawingCache()
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        return baos.toByteArray()
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

//            commentView.gComment.text = comment.text!!
            return commentView
        }
    }

}
