package com.example.gg.data.model

import com.google.firebase.database.Exclude

data class Comment(
    val id: String = "",
    val gameId: String = "",
    val text: String = "",
    val userId: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "gameId" to gameId,
            "id" to id,
            "text" to text,
            "user" to userId
        )
    }
}