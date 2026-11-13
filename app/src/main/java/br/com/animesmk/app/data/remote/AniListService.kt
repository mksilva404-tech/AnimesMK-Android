package br.com.animesmk.app

import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class AniListAnime(
    val id: Int,
    val title: String,
    val description: String,
    val genres: String,
    val episodes: Int,
    val year: Int,
    val cover: String
)

object AniListService {

    fun search(
        term: String,
        onResult: (List<AniListAnime>) -> Unit,
        onError: (String) -> Unit
    ) {
        Thread {
            try {
                val query = """
                    query (${'$'}search: String) {
                      Page(perPage: 20) {
                        media(search: ${'$'}search, type: ANIME, isAdult: false) {
                          id
                          title { romaji english }
                          description(asHtml: false)
                          genres
                          episodes
                          seasonYear
                          coverImage { large }
                        }
                      }
                    }
                """.trimIndent()

                val body = JSONObject()
                    .put("query", query)
                    .put("variables", JSONObject().put("search", term))

                val connection = URL("https://graphql.anilist.co")
                    .openConnection() as HttpURLConnection

                connection.requestMethod = "POST"
                connection.connectTimeout = 15000
                connection.readTimeout = 15000
                connection.doOutput = true
                connection.setRequestProperty(
                    "Content-Type", "application/json"
                )

                connection.outputStream.use {
                    it.write(body.toString().toByteArray(Charsets.UTF_8))
                }

                val response = connection.inputStream.bufferedReader().use {
                    it.readText()
                }

                connection.disconnect()

                val media = JSONObject(response)
                    .getJSONObject("data")
                    .getJSONObject("Page")
                    .getJSONArray("media")

                val results = (0 until media.length()).map { index ->
                    val item = media.getJSONObject(index)
                    val titles = item.getJSONObject("title")

                    AniListAnime(
                        id = item.getInt("id"),
                        title = titles.optString("english")
                            .takeIf { it.isNotBlank() }
                            ?: titles.optString("romaji"),
                        description = item.optString("description"),
                        genres = (0 until item.getJSONArray("genres").length())
                            .map { item.getJSONArray("genres").getString(it) }
                            .joinToString(" • "),
                        episodes = item.optInt("episodes"),
                        year = item.optInt("seasonYear"),
                        cover = item.getJSONObject("coverImage")
                            .optString("large")
                    )
                }

                Handler(Looper.getMainLooper()).post {
                    onResult(results)
                }

            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    onError(e.message ?: "Erro ao consultar catálogo")
                }
            }
        }.start()
    }
}
