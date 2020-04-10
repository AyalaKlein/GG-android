package com.example.gg.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import java.io.Serializable

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "comment_game_id") var gameId: String = "",
    @ColumnInfo(name = "comment_text") var text: String = "",
    @ColumnInfo(name = "comment_user") var user: String = ""
): Serializable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "gameId" to gameId,
            "text" to text,
            "user" to user
        )
    }
}