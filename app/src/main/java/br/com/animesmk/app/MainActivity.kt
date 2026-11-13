package br.com.animesmk.app

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Anime(val title: String, val genre: String, val description: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AnimeApp() }
    }
}

@Composable
fun AnimeApp() {
    val animes = listOf(
        Anime("Naruto", "Ação", "A jornada de um jovem ninja."),
        Anime("One Piece", "Aventura", "Uma aventura em busca do maior tesouro."),
        Anime("Attack on Titan", "Ação", "A humanidade luta pela sobrevivência."),
        Anime("Demon Slayer", "Fantasia", "Um jovem enfrenta demônios."),
        Anime("Jujutsu Kaisen", "Fantasia", "Uma batalha contra maldições."),
        Anime("Death Note", "Suspense", "Um caderno com poderes misteriosos.")
    )

    var search by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Todos") }
    var favorites by remember { mutableStateOf(setOf<String>()) }
    var selected by remember { mutableStateOf<Anime?>(null) }
    var showFavorites by remember { mutableStateOf(false) }

    val categories = listOf("Todos", "Ação", "Aventura", "Fantasia", "Suspense")

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFB388FF),
            background = Color(0xFF101018),
            surface = Color(0xFF20202B)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF101018)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text(
                    "ANIMESMK",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFFB388FF)
                )
                    Image(
                        painter = painterResource(id = R.drawable.animesmk_banner),
                        contentDescription = "Capa personalizada AnimesMK",
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )
                Text("Seu universo de animes", color = Color.LightGray)

                Spacer(Modifier.height(18.dp))

                if (selected != null) {
                    TextButton(onClick = { selected = null }) {
                        Text("← Voltar ao catálogo")
                    }
                    Text(selected!!.title, style = MaterialTheme.typography.headlineMedium)
                    Text(selected!!.genre, color = Color(0xFFB388FF))
                    Spacer(Modifier.height(12.dp))
                    Text(selected!!.description, color = Color.White)
                    Spacer(Modifier.height(16.dp))
                    Text("Episódios e reprodução serão conectados na próxima etapa.")
                } else {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        label = { Text("Pesquisar anime") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { showFavorites = false }) {
                            Text("Catálogo")
                        }
                        TextButton(onClick = { showFavorites = true }) {
                            Text("Favoritos (${favorites.size})")
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        categories.forEach { item ->
                            FilterChip(
                                selected = category == item,
                                onClick = { category = item },
                                label = { Text(item) }
                            )
                        }
                    }

                    val filtered = animes.filter {
                        it.title.contains(search, ignoreCase = true) &&
                        (category == "Todos" || it.genre == category) &&
                        (!showFavorites || it.title in favorites)
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(filtered) { anime ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF242432)
                                ),
                                onClick = { selected = anime }
                            ) {
                                Row(
                                    Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            anime.title,
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Text(anime.genre, color = Color(0xFFB388FF))
                                        Text(anime.description, color = Color.LightGray)
                                    }
                                    TextButton(onClick = {
                                        favorites = if (anime.title in favorites)
                                            favorites - anime.title
                                        else favorites + anime.title
                                    }) {
                                        Text(if (anime.title in favorites) "★" else "☆")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
