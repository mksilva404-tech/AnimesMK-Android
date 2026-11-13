package br.com.animesmk.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun AccountScreen() {
    Column(
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(22.dp)
    ) {
        Text(
            "Minha Conta",
            color = Color.White,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

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
            Text(
                it,
                color = Color.White,
                fontSize = 17.sp,
                modifier = Modifier.fillMaxWidth()
                    .clickable { }
                    .padding(vertical = 18.dp)
            )
            HorizontalDivider(color = Color.DarkGray)
        }
    }
}
