package br.com.animesmk.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.runtime.DisposableEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AnimeApp() }
    }
}

@Composable
fun AnimeApp() {
    val animes = listOf(
        Anime("Black Clover", "Ação • Fantasia", "Asta e Yuno sonham em se tornar o Rei Mago.", "14", Color(0xFF54251C)),
        Anime("Naruto", "Ação • Aventura", "A jornada de um jovem ninja.", "12", Color(0xFF9B4B16)),
        Anime("One Piece", "Aventura • Fantasia", "Luffy parte em busca do maior tesouro.", "12", Color(0xFF245A80)),
        Anime("Attack on Titan", "Ação • Drama", "A humanidade luta pela sobrevivência.", "16", Color(0xFF35465E)),
        Anime("Demon Slayer", "Ação • Fantasia", "Tanjiro enfrenta demônios para salvar sua irmã.", "16", Color(0xFF49304C)),
        Anime("Jujutsu Kaisen", "Ação • Sobrenatural", "Uma batalha contra maldições.", "16", Color(0xFF35405E)),
        Anime("Fairy Tail", "Aventura • Magia", "As aventuras da guilda Fairy Tail.", "14", Color(0xFF803344)),
        Anime("Death Note", "Suspense • Mistério", "Um caderno capaz de decidir destinos.", "16", Color(0xFF343434)),
        Anime("My Hero Academia", "Ação • Heróis", "Jovens aprendem a ser grandes heróis.", "14", Color(0xFF315B39)),
        Anime("Overlord", "Fantasia • Ação", "Um jogador fica preso em um mundo virtual.", "16", Color(0xFF55452C))
    )

    var tab by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Anime?>(null) }
    var search by remember { mutableStateOf("") }
    var favorites by remember { mutableStateOf(setOf<String>()) }
    var playing by remember { mutableStateOf(false) }

    AnimesMKTheme {
        Scaffold(
            containerColor = black,
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF202024)) {
                    val labels = listOf("Início", "Minhas Listas", "Explorar", "Simulcasts", "Conta")
                    val icons = listOf("⌂", "▣", "▦", "✧", "●")
                    labels.forEachIndexed { index, label ->
                        NavigationBarItem(
                            selected = tab == index,
                            onClick = {
                                tab = index
                                selected = null
                                playing = false
                            },
                            icon = { Text(icons[index], fontSize = 23.sp) },
                            label = { Text(label, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = orange,
                                selectedTextColor = orange,
                                indicatorColor = Color(0xFF39251B),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            )
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(black)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("ANIMES", color = orange, fontSize = 25.sp, fontWeight = FontWeight.Black)
                    Text("MK", color = purple, fontSize = 25.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.weight(1f))
                    Text("⌕", color = Color.White, fontSize = 30.sp,
                        modifier = Modifier.clickable { tab = 2 })
                }

                if (selected != null) {
                    AnimeDetail(
                        anime = selected!!,
                        favorite = selected!!.title in favorites,
                        playing = playing,
                        onFavorite = {
                            favorites = if (selected!!.title in favorites)
                                favorites - selected!!.title else favorites + selected!!.title
                        },
                        onPlay = { playing = !playing },
                        onBack = { selected = null; playing = false }
                    )
                } else {
                    when (tab) {
                        0 -> HomeScreen(animes, favorites, { selected = it }, { tab = 2 })
                        1 -> CatalogScreen(
                            "Minhas Listas",
                            animes.filter { it.title in favorites },
                            search,
                            { search = it },
                            { selected = it }
                        )
                        2 -> OnlineCatalogScreen(onSelect = { selected = it })
                        3 -> HomeScreen(animes.reversed(), favorites, { selected = it }, { tab = 2 }, true)
                        else -> AccountScreen()
                    }
                }
            }
        }
    }
}
