package br.com.animesmk.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun CatalogScreen(
    title: String,
    animes: List<Anime>,
    search: String,
    onSearch: (String) -> Unit,
    onSelect: (Anime) -> Unit
) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Text(
            title,
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = search,
            onValueChange = onSearch,
            label = { Text("Pesquisar anime") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(animes) { anime ->
                Row(
                    Modifier.fillMaxWidth()
                        .background(panel, RoundedCornerShape(10.dp))
                        .clickable { onSelect(anime) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(65.dp, 85.dp)
                            .background(anime.color, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            anime.title.take(2),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        Modifier.padding(start = 14.dp).weight(1f)
                    ) {
                        Text(
                            anime.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(anime.genre, color = purple)
                        Text("${anime.age} anos", color = Color.LightGray)
                    }

                    Text("›", color = orange, fontSize = 28.sp)
                }
            }
        }
    }
}
