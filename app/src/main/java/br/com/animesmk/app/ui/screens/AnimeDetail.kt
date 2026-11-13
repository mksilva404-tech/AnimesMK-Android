package br.com.animesmk.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

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
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            "← Voltar",
            color = orange,
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(Modifier.height(18.dp))

        Box(
            Modifier.fillMaxWidth().height(230.dp)
                .background(
                    Brush.linearGradient(listOf(anime.color, Color.Black)),
                    RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                anime.title,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(Modifier.height(18.dp))

        Text(
            anime.title,
            color = Color.White,
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "${anime.age} • ${anime.genre} • Dub | Leg",
            color = purple
        )

        Spacer(Modifier.height(12.dp))

        Text(
            anime.description,
            color = Color.LightGray,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onPlay,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = orange
                )
            ) {
                Text(
                    if (playing) "Fechar player" else "▶  Assistir Agora",
                    color = Color.Black
                )
            }

            TextButton(onClick = onFavorite) {
                Text(
                    if (favorite) "★" else "☆",
                    color = orange,
                    fontSize = 27.sp
                )
            }
        }

        if (playing) {
            Spacer(Modifier.height(15.dp))
            Text("VÍDEO DE TESTE DO PLAYER", color = purple)

            AnimePlayer(
                "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
            )
        } else {
            Spacer(Modifier.height(25.dp))
            Text(
                "Temporada 1",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Os episódios serão adicionados quando as fontes autorizadas forem conectadas.",
                color = Color.LightGray
            )
        }
    }
}
