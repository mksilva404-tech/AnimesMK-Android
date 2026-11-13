package br.com.animesmk.app

import androidx.compose.ui.graphics.Color

data class Anime(
    val title: String,
    val genre: String,
    val description: String,
    val age: String,
    val color: Color,
    val aniListId: Int? = null,
    val coverUrl: String = "",
    val episodes: Int = 0,
    val year: Int = 0
)
