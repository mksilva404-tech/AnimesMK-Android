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

data class Anime(
    val title: String,
    val genre: String,
    val description: String,
    val age: String,
    val color: Color
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AnimeApp() }
    }
}

private val orange = Color(0xFFFF6900)
private val purple = Color(0xFFB388FF)
private val black = Color(0xFF080808)
private val panel = Color(0xFF202020)

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

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = orange,
            secondary = purple,
            background = black,
            surface = panel
        )
    ) {
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
                        2 -> CatalogScreen(
                            "Explorar",
                            animes.filter { it.title.contains(search, true) },
                            search,
                            { search = it },
                            { selected = it }
                        )
                        3 -> HomeScreen(animes.reversed(), favorites, { selected = it }, { tab = 2 }, true)
                        else -> AccountScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    animes: List<Anime>,
    favorites: Set<String>,
    onSelect: (Anime) -> Unit,
    onExplore: () -> Unit,
    simulcasts: Boolean = false
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Box(
                    Modifier.fillMaxWidth().height(260.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF5B210B), Color(0xFF17101F), Color(0xFF311C45))),
                            RoundedCornerShape(18.dp)
                        )
                        .clickable { onSelect(animes.first()) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text("DESTAQUE ANIMESMK", color = orange, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(20.dp))
                        Text(
                            if (simulcasts) "Novos episódios" else "SEU PRÓXIMO ANIME FAVORITO",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(animes.first().title, color = purple, fontSize = 22.sp)
                        Spacer(Modifier.height(20.dp))
                        Button(
                            onClick = { onSelect(animes.first()) },
                            colors = ButtonDefaults.buttonColors(containerColor = orange)
                        ) { Text("▶  Assistir Agora", color = Color.Black) }
                    }
                }
                Spacer(Modifier.height(25.dp))
            }
        }
        item { AnimeSection("Animes em alta no Brasil", animes.take(5), onSelect) }
        item { AnimeSection("Inspirados em você", animes.drop(3).take(5), onSelect) }
        item { AnimeSection("Vilões icônicos", animes.reversed().take(5), onSelect) }
        item { AnimeSection("Romances e aventuras", animes.drop(1).take(5), onSelect) }
        item { AnimeSection("Continue assistindo", animes.take(4), onSelect) }
        item {
            TextButton(onClick = onExplore, modifier = Modifier.fillMaxWidth()) {
                Text("VER TODO O CATÁLOGO  →", color = orange)
            }
        }
    }
}

@Composable
fun AnimeSection(title: String, animes: List<Anime>, onSelect: (Anime) -> Unit) {
    Column(Modifier.padding(bottom = 22.dp)) {
        Text(
            title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(animes) { anime ->
                Column(
                    Modifier.width(145.dp).clickable { onSelect(anime) }
                ) {
                    Box(
                        Modifier.fillMaxWidth().height(195.dp)
                            .background(
                                Brush.linearGradient(listOf(anime.color, Color(0xFF151515))),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            anime.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(Modifier.height(7.dp))
                    Text(anime.title, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${anime.age} • Dub | Leg", color = Color.LightGray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun CatalogScreen(
    title: String,
    animes: List<Anime>,
    search: String,
    onSearch: (String) -> Unit,
    onSelect: (Anime) -> Unit
) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Text(title, color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = search,
            onValueChange = onSearch,
            label = { Text("Pesquisar anime") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(animes) { anime ->
                Row(
                    Modifier.fillMaxWidth().background(panel, RoundedCornerShape(10.dp))
                        .clickable { onSelect(anime) }.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(65.dp, 85.dp)
                            .background(anime.color, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(anime.title.take(2), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Column(Modifier.padding(start = 14.dp).weight(1f)) {
                        Text(anime.title, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(anime.genre, color = purple)
                        Text("${anime.age} anos", color = Color.LightGray)
                    }
                    Text("›", color = orange, fontSize = 28.sp)
                }
            }
        }
    }
}

@Composable
fun AnimeDetail(
    anime: Anime,
    favorite: Boolean,
    playing: Boolean,
    onFavorite: () -> Unit,
    onPlay: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("← Voltar", color = orange, modifier = Modifier.clickable { onBack() })
        Spacer(Modifier.height(18.dp))
        Box(
            Modifier.fillMaxWidth().height(230.dp)
                .background(
                    Brush.linearGradient(listOf(anime.color, Color.Black)),
                    RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(anime.title, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(18.dp))
        Text(anime.title, color = Color.White, fontSize = 29.sp, fontWeight = FontWeight.Bold)
        Text("${anime.age} • ${anime.genre} • Dub | Leg", color = purple)
        Spacer(Modifier.height(12.dp))
        Text(anime.description, color = Color.LightGray, fontSize = 16.sp)
        Spacer(Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onPlay,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = orange)
            ) {
                Text(if (playing) "Fechar player" else "▶  Assistir Agora", color = Color.Black)
            }
            TextButton(onClick = onFavorite) {
                Text(if (favorite) "★" else "☆", color = orange, fontSize = 27.sp)
            }
        }
        if (playing) {
            Spacer(Modifier.height(15.dp))
            Text("VÍDEO DE TESTE DO PLAYER", color = purple)
            AnimePlayer("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
        } else {
            Spacer(Modifier.height(25.dp))
            Text("Temporada 1", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Text("Os episódios serão adicionados quando as fontes autorizadas forem conectadas.", color = Color.LightGray)
        }
    }
}

@Composable
fun AccountScreen() {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp)
    ) {
        Text("Minha Conta", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(30.dp))
        Text("●  Perfil AnimesMK", color = orange, fontSize = 22.sp)
        Spacer(Modifier.height(25.dp))
        listOf(
            "Meu perfil",
            "Idioma de áudio",
            "Idioma de legenda",
            "Qualidade de reprodução",
            "Notificações",
            "Downloads",
            "Privacidade",
            "Suporte",
            "Sobre o AnimesMK"
        ).forEach {
            Text(it, color = Color.White, fontSize = 17.sp, modifier = Modifier.fillMaxWidth().clickable { }.padding(vertical = 18.dp))
            HorizontalDivider(color = Color.DarkGray)
        }
    }
}

@Composable
fun AnimePlayer(videoUrl: String) {
    val context = LocalContext.current
    val player = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
        }
    }
    DisposableEffect(player) {
        onDispose { player.release() }
    }
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                this.player = player
                useController = true
            }
        },
        update = { it.player = player },
        modifier = Modifier.fillMaxWidth().height(230.dp)
    )
}
