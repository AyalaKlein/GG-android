package com.example.gg.ui.newGame

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gg.data.GameRepository
import com.example.gg.data.model.Game
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@InternalCoroutinesApi
class NewGameViewModel(private val gameRepository: GameRepository): ViewModel() {
    private val _createForm = MutableLiveData<Boolean>()
    val createFormState: LiveData<Boolean> = _createForm

    fun createGame(genre: String, name: String, score: Int, description: String, uid: String, image: ByteArray): Task<String> {
        return gameRepository.createGame(genre, name, score, description, uid, image)
    }

    fun updateGame(key: String, genre: String, name: String, score: Int, description: String, uid: String, image: ByteArray): Task<String> {
        return gameRepository.updateGame(key, genre, name, score, description, uid, image)
    }

    fun deleteGame(game: Game): Task<String> {
        return gameRepository.deleteGame(game)
    }

    fun saveComment(gameId: String, text: String, uid: String): Task<String> {
        return gameRepository.saveComment(gameId, text, uid)
    }

    fun createGameDataChanged(gameName: String?, gameImage: Drawable?, gameGenre: String?, gameScore: String?, gameDesc: String?): Boolean {
        _createForm.value = gameName!!.isNotEmpty() &&
                gameGenre!!.isNotEmpty() &&
                gameDesc!!.isNotEmpty() &&
                gameScore!!.isNotEmpty() && gameImage != null

        return _createForm.value!!
    }
}