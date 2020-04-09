package com.example.gg.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import java.io.Serializable

@Entity(tableName = "games")
data class Game(
    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "game_genre") var genre: String = "",
    @ColumnInfo(name = "game_name") var name: String = "",
    @ColumnInfo(name = "game_score") var score: Int = -1,
    @ColumnInfo(name = "game_description") var description: String = "",
    @ColumnInfo(name = "game_user_id") var userId: String = "",
    @Ignore var Comments: HashMap<String, Comment>? = HashMap<String, Comment>()
) : Serializable {
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