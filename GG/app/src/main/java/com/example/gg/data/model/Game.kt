package com.example.gg.data.model

import androidx.room.*
import com.google.firebase.database.Exclude
import java.io.Serializable

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "game_genre") var genre: String = "",
    @ColumnInfo(name = "game_name") var name: String = "",
    @ColumnInfo(name = "game_score") var score: Int = -1,
    @ColumnInfo(name = "game_description") var description: String = "",
    @ColumnInfo(name = "game_user_id") var userId: String = "",
    @ColumnInfo(name = "game_status") var status: Int = ModelStatus.NO_CHANGE.ordinal,
    @Ignore var Comments: HashMap<String, Comment>? = HashMap<String, Comment>(),
    @Relation(parentColumn = "id", entityColumn = "comment_game_id", entity = Comment::class) @Ignore var listComments: List<Comment>? = listOf(),
    @Ignore var gameImage: ByteArray? = null
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