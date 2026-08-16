package com.synapse.mobile.features.skills.media.provider

import com.synapse.mobile.features.skills.media.resolver.MediaSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL

class BackendMediaProvider(
    private val baseUrl: String
) : MediaProvider {

    override suspend fun search(
        query: String
    ): MediaSource? = withContext(Dispatchers.IO) {

        var connection: HttpURLConnection? = null

        try {

            val encodedQuery =
                URLEncoder.encode(
                    query.trim(),
                    "UTF-8"
                )

            val url =
                URL(
                    "$baseUrl/api/media/search?q=$encodedQuery"
                )

            connection =
                url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000

            val responseCode =
                connection.responseCode

            if (responseCode !in 200..299) {
                return@withContext null
            }

            val response =
                connection.inputStream
                    .bufferedReader()
                    .use { it.readText() }

            val json =
                JSONObject(response)

            if (!json.optBoolean("success", false)) {
                return@withContext null
            }

            val media =
                json.optJSONObject("media")
                    ?: return@withContext null

            val title =
                media.optString("title")

            val uri =
                media.optString("uri")

            if (
                title.isBlank() ||
                uri.isBlank()
            ) {
                return@withContext null
            }

            MediaSource(
                title = title,
                uri = uri
            )

        } catch (e: Exception) {

            e.printStackTrace()

            null

        } finally {

            connection?.disconnect()
        }
    }
}