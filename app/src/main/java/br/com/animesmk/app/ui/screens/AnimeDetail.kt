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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage

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
        Modifier
            .fillMaxSize()
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
            Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    Brush.linearGradient(
                        listOf(anime.color, Color.Black)
                    ),
                    RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (anime.coverUrl.isNotBlank()) {
                AsyncImage(
                    model = anime.coverUrl,
                    contentDescription = "Capa de ${anime.title}",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                anime.title,
                color = Color.White,
                fontSize = 27.sp,
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

        Spacer(Modifier.height(8.dp))

        Text(
            anime.genre,
            color = purple
        )

        Spacer(Modifier.height(10.dp))

        Text(
            buildString {
                append("Classificação: ${anime.age}")
                if (anime.year > 0) append(" • ${anime.year}")
                if (anime.episodes > 0) append(" • ${anime.episodes} episódios")
            },
            color = Color.LightGray,
            fontSize = 13.sp
        )

        if (anime.aniListId != null) {
            Text(
                "AniList ID: ${anime.aniListId}",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(15.dp))

        Text(
            "Sinopse",
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(6.dp))

        Text(
            anime.description,
            color = Color.LightGray,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(22.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onPlay,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = orange
                )
            ) {
                Text(
                    if (playing) "Fechar informações"
                    else "▶  Opções de reprodução",
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
            Spacer(Modifier.height(18.dp))

            Text(
                "REPRODUÇÃO",
                color = purple,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Este anime ainda não possui uma fonte de reprodução conectada. " +
                    "O catálogo fornece informações e capas, mas não hospeda episódios.",
                color = Color.LightGray
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "As opções oficiais de streaming poderão aparecer aqui quando " +
                    "forem disponibilizadas pela fonte de dados.",
                color = Color.Gray,
                fontSize = 13.sp
            )
        } else {
            Spacer(Modifier.height(22.dp))

            Text(
                "Episódios",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                if (anime.episodes > 0) {
                    "${anime.episodes} episódios cadastrados no AniList. " +
                        "A lista de reprodução ainda será conectada."
                } else {
                    "A quantidade de episódios não está informada no catálogo."
                },
                color = Color.LightGray
            )
        }

        Spacer(Modifier.height(25.dp))
    }
}
