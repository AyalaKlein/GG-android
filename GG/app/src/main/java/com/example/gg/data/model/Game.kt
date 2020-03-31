package com.example.gg.data.model

import com.google.firebase.database.Exclude

data class Game(
    val id: String = "",
    val genre: String = "",
    val name: String = "",
    val score: Int = -1,
    val description: String = "",
    val userId: String = "",
    val Comments: HashMap<String, Comment>? = HashMap<String, Comment>()
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "genre" to genre,
            "name" to name,
            "score" to score,
            "description" to description,
            "userId" to userId,
            "Comments" to Comments
        )
    }
}