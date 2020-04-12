package com.example.gg.data.internetConnection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class AppInternetConnection(private val context: Context) {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isConnected(): Boolean {
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

        return activeNetwork?.isConnectedOrConnecting == true
    }
}