package com.example.gg.data.model

import com.google.firebase.database.Exclude
import java.io.Serializable

data class Comment(
    val id: String = "",
    val gameId: String = "",
    var text: String = "",
    val userId: String = ""
): Serializable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "gameId" to gameId,
            "text" to text,
            "user" to userId
        )
    }
}