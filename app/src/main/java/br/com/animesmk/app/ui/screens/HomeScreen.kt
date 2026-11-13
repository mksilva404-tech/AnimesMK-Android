package br.com.animesmk.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*

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
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF5B210B),
                                    Color(0xFF17101F),
                                    Color(0xFF311C45)
                                )
                            ),
                            RoundedCornerShape(18.dp)
                        )
                        .clickable { onSelect(animes.first()) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(
                            "DESTAQUE ANIMESMK",
                            color = orange,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(20.dp))
                        Text(
                            if (simulcasts) "Novos episódios"
                            else "SEU PRÓXIMO ANIME FAVORITO",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            animes.first().title,
                            color = purple,
                            fontSize = 22.sp
                        )
                        Spacer(Modifier.height(20.dp))
                        Button(
                            onClick = { onSelect(animes.first()) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = orange
                            )
                        ) {
                            Text("▶  Assistir Agora", color = Color.Black)
                        }
                    }
                }
                Spacer(Modifier.height(25.dp))
            }
        }

        item {
            AnimeSection("Animes em alta no Brasil", animes.take(5), onSelect)
        }
        item {
            AnimeSection("Inspirados em você", animes.drop(3).take(5), onSelect)
        }
        item {
            AnimeSection("Vilões icônicos", animes.reversed().take(5), onSelect)
        }
        item {
            AnimeSection("Romances e aventuras", animes.drop(1).take(5), onSelect)
        }
        item {
            AnimeSection("Continue assistindo", animes.take(4), onSelect)
        }
        item {
            TextButton(
                onClick = onExplore,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("VER TODO O CATÁLOGO  →", color = orange)
            }
        }
    }
}

@Composable
fun AnimeSection(
    title: String,
    animes: List<Anime>,
    onSelect: (Anime) -> Unit
) {
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
                                Brush.linearGradient(
                                    listOf(anime.color, Color(0xFF151515))
                                ),
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
                    Text(
                        anime.title,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "${anime.age} • Dub | Leg",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
