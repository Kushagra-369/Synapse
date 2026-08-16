package com.synapse.mobile.features.skills.media.provider

import android.util.Log
import com.synapse.mobile.features.skills.media.resolver.MediaSearchResult
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
    ): MediaSearchResult? = withContext(Dispatchers.IO) {

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

            Log.d(
                "SYNAPSE_MEDIA",
                "BACKEND HTTP CODE: $responseCode"
            )

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

            // -----------------------------
            // BEST MATCH
            // -----------------------------

            val media =
                json.optJSONObject("media")
                    ?: return@withContext null

            fun parseMedia(
                jsonObject: JSONObject
            ): MediaSource? {

                val title =
                    jsonObject.optString("title")

                val uri =
                    jsonObject.optString("uri")

                if (
                    title.isBlank() ||
                    uri.isBlank()
                ) {
                    return null
                }

                val artist =
                    jsonObject
                        .optString("artist")
                        .takeIf {
                            it.isNotBlank()
                        }

                val album =
                    jsonObject
                        .optString("album")
                        .takeIf {
                            it.isNotBlank()
                        }

                val duration =
                    if (
                        jsonObject.has("duration") &&
                        !jsonObject.isNull("duration")
                    ) {
                        jsonObject.optLong("duration")
                    } else {
                        null
                    }

                val coverArt =
                    jsonObject
                        .optString("coverArt")
                        .takeIf {
                            it.isNotBlank()
                        }

                return MediaSource(
                    title = title,
                    uri = uri,
                    artist = artist,
                    album = album,
                    duration = duration,
                    coverArt = coverArt
                )
            }

            val bestMatch =
                parseMedia(media)
                    ?: return@withContext null

            Log.d(
                "SYNAPSE_MEDIA",
                "BACKEND RESULT: " +
                        "title=${bestMatch.title} " +
                        "artist=${bestMatch.artist} " +
                        "album=${bestMatch.album}"
            )

            // -----------------------------
            // ALTERNATIVE RESULTS
            // -----------------------------

            val alternatives =
                mutableListOf<MediaSource>()

            val resultsArray =
                json.optJSONArray("results")

            if (resultsArray != null) {

                for (i in 0 until resultsArray.length()) {

                    val resultObject =
                        resultsArray.optJSONObject(i)
                            ?: continue

                    val result =
                        parseMedia(resultObject)
                            ?: continue

                    // Don't duplicate best match
                    if (
                        result.uri != bestMatch.uri
                    ) {
                        alternatives.add(result)
                    }
                }
            }

            Log.d(
                "SYNAPSE_MEDIA",
                "ALTERNATIVES FOUND: ${alternatives.size}"
            )

            return@withContext MediaSearchResult(
                bestMatch = bestMatch,
                alternatives = alternatives
            )

        } catch (e: Exception) {

            Log.e(
                "SYNAPSE_MEDIA",
                "BACKEND ERROR: ${e.message}",
                e
            )

            null

        } finally {

            connection?.disconnect()
        }
    }
}