package com.example.gg

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.ViewModelProviders
import com.example.gg.data.dataSource.FireBaseDataSource
import com.example.gg.data.model.Game
import com.example.gg.ui.gameDetails.GameDetails
import com.example.gg.ui.newGame.NewGameViewModel
import com.example.gg.ui.newGame.NewGameViewModelFactory
import kotlinx.android.synthetic.main.activity_comment_pop_up.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@InternalCoroutinesApi
class CommentPopUpActivity : AppCompatActivity() {
    private var popupButton = ""
    private var darkStatusBar = false

    private lateinit var newGameViewModel: NewGameViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_comment_pop_up)


        newGameViewModel  = ViewModelProviders.of(this, NewGameViewModelFactory(applicationContext)).get(
            NewGameViewModel::class.java)

        // ...
        // Get the data
        val bundle = intent.extras
        popupButton = bundle?.getString("popupbtn", "Button") ?: ""
        darkStatusBar = bundle?.getBoolean("darkstatusbar", false) ?: false

        popup_window_button.text = popupButton


        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // If you want dark status bar, set darkStatusBar to true
                if (darkStatusBar) {
                    this.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                this.window.statusBarColor = Color.TRANSPARENT
                setWindowFlag(this, false)
            }

            val alpha = 100 //between 0-255
            val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
            colorAnimation.duration = 500 // milliseconds
            colorAnimation.addUpdateListener { animator ->
                popup_window_background.setBackgroundColor(animator.animatedValue as Int)
            }
            colorAnimation.start()

            popup_window_view_with_border.alpha = 0f
            popup_window_view_with_border.animate().alpha(1f).setDuration(500).setInterpolator(
                DecelerateInterpolator()
            ).start()


            val game = intent.getSerializableExtra("sGame") as Game
            popup_window_button.setOnClickListener {
                saveComment(game.id)
                val intent = Intent(applicationContext, GameDetails::class.java)
                intent.putExtra("sGame", game)
                intent.putExtra("newComm", popup_window_text.editText?.text.toString())
                startActivity(intent)
                onBackPressed()
                finish()
            }
        }
    }

    private fun saveComment(gameId: String) {
            newGameViewModel.saveComment(gameId, popup_window_text.editText?.text.toString(), FireBaseDataSource.Auth.currentUser!!.email.toString()).addOnCompleteListener {
                Toast.makeText(applicationContext, "You Comment Saved",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // error not good
            }
        }

    override fun onBackPressed() {

        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popup_window_background.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        // Fade animation for the Popup Window when you press the back button
        popup_window_view_with_border.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }

    private fun setWindowFlag(activity: Activity, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            } else {
                winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
            }
            win.attributes = winParams
        }
}

