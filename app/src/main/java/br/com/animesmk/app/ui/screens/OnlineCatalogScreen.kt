package br.com.animesmk.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun OnlineCatalogScreen(onSelect: (Anime) -> Unit) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<AniListAnime>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var searched by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Text(
            "Explorar animes",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Nome do anime") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            Button(
                enabled = query.isNotBlank() && !loading,
                onClick = {
                    loading = true
                    error = null
                    searched = true
                    results = emptyList()

                    AniListService.search(
                        query.trim(),
                        onResult = {
                            results = it
                            loading = false
                        },
                        onError = {
                            error = it
                            loading = false
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6900)
                )
            ) {
                Text("Buscar", color = Color.Black)
            }
        }

        Spacer(Modifier.height(15.dp))

        if (loading) {
            CircularProgressIndicator(color = Color(0xFFFF6900))
            Text("Buscando animes...", color = Color.White)
        }

        error?.let {
            Text("Erro: $it", color = Color.Red)
        }

        if (searched && !loading && error == null && results.isEmpty()) {
            Text("Nenhum anime encontrado.", color = Color.LightGray)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(results, key = { it.id }) { item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF202020),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            onSelect(
                                Anime(
                                    title = item.title,
                                    genre = item.genres.ifBlank { "Anime" },
                                    description = item.description.ifBlank {
                                        "Sinopse não disponível."
                                    },
                                    age = "Não informada",
                                    color = Color(0xFF54251C),
                                    aniListId = item.id,
                                    coverUrl = item.cover,
                                    episodes = item.episodes,
                                    year = item.year
                                )
                            )
                        }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = item.cover,
                        contentDescription = item.title,
                        modifier = Modifier.size(88.dp, 124.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            item.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            item.genres.ifBlank { "Gênero não informado" },
                            color = Color(0xFFB388FF),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            if (item.year > 0) item.year.toString()
                            else "Ano não informado",
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )

                        Text(
                            if (item.episodes > 0) {
                                "${item.episodes} episódios"
                            } else {
                                "Episódios não informados"
                            },
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
