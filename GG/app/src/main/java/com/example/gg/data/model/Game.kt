package com.example.gg.data.model

data class Game(
    val id: String = "",
    val genre: String = "",
    val name: String = "",
    val score: Int = -1,
    val description: String = "",
    val userId: String = "",
    val comments: List<Comment>? = null
)