package com.example.gg.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gg.data.GameRepository
import com.example.gg.data.dataSource.GameDataSource
import com.example.gg.data.dbAccess.dataAccess.GameDataAccess
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@InternalCoroutinesApi
class MainViewModelFactory(val context: Context, private val callback: (Unit)->Unit): ViewModelProvider.Factory {
    private var mvm: MainViewModel? = null
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            if (mvm == null)
                mvm = MainViewModel(
                    gameRepository = GameRepository(
                        dataSource = GameDataSource(callback),
                        context = context
                    )
                )

            return mvm as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}