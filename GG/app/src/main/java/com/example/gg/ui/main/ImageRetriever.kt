package com.example.gg.ui.main

import android.net.Uri
import com.google.android.gms.tasks.Task

interface ImageRetriever {
    fun getImageUrl(uid: String): Task<ByteArray?>
}