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

        Log.d(
            "SYNAPSE_MEDIA",
            "BACKEND PROVIDER CALLED: query=$query baseUrl=$baseUrl"
        )

        var connection: HttpURLConnection? = null

        try {

            // =====================================================
            // 1. ENCODE SEARCH QUERY
            // =====================================================

            val encodedQuery =
                URLEncoder.encode(
                    query.trim(),
                    "UTF-8"
                )

            val url =
                URL(
                    "$baseUrl/api/media/search?q=$encodedQuery"
                )

            Log.d(
                "SYNAPSE_MEDIA",
                "BACKEND URL: $url"
            )

            // =====================================================
            // 2. HTTP REQUEST
            // =====================================================

            connection =
                url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connectTimeout = 15_000
            connection.readTimeout = 30_000
            connection.setRequestProperty(
                "Accept",
                "application/json"
            )

            val responseCode =
                connection.responseCode

            Log.d(
                "SYNAPSE_MEDIA",
                "BACKEND HTTP CODE: $responseCode"
            )

            if (responseCode !in 200..299) {

                Log.e(
                    "SYNAPSE_MEDIA",
                    "BACKEND REQUEST FAILED: HTTP $responseCode"
                )

                return@withContext null
            }

            // =====================================================
            // 3. READ RESPONSE
            // =====================================================

            val response =
                connection.inputStream
                    .bufferedReader()
                    .use {
                        it.readText()
                    }

            Log.d(
                "SYNAPSE_MEDIA",
                "BACKEND RAW RESPONSE: $response"
            )

            if (response.isBlank()) {

                Log.e(
                    "SYNAPSE_MEDIA",
                    "BACKEND RESPONSE EMPTY"
                )

                return@withContext null
            }

            val json =
                JSONObject(response)

            // =====================================================
            // 4. CHECK SUCCESS
            // =====================================================

            if (!json.optBoolean("success", false)) {

                Log.e(
                    "SYNAPSE_MEDIA",
                    "BACKEND SUCCESS = FALSE"
                )

                return@withContext null
            }

            // =====================================================
            // 5. PARSE MEDIA OBJECT
            // =====================================================

            fun parseMedia(
                jsonObject: JSONObject
            ): MediaSource? {

                val title =
                    jsonObject
                        .optString("title")
                        .trim()

                val backendUri =
                    jsonObject
                        .optString("uri")
                        .trim()

                // -------------------------------------------------
                // IMPORTANT:
                //
                // Backend already returns:
                //
                // http://192.168.1.5:6969/api/media/stream/g9E62
                //
                // DO NOT convert it again.
                // -------------------------------------------------

                if (title.isBlank()) {

                    Log.w(
                        "SYNAPSE_MEDIA",
                        "SKIPPING MEDIA: title empty"
                    )

                    return null
                }

                if (backendUri.isBlank()) {

                    Log.w(
                        "SYNAPSE_MEDIA",
                        "SKIPPING MEDIA: uri empty"
                    )

                    return null
                }

                // -------------------------------------------------
                // Use backend URI directly
                // -------------------------------------------------

                val uri =
                    when {

                        backendUri.startsWith("http://") ||
                                backendUri.startsWith("https://") -> {

                            backendUri
                        }

                        else -> {

                            // Safety fallback if backend somehow
                            // returns only a path.
                            "$baseUrl/${backendUri.trimStart('/')}"
                        }
                    }

                val artist =
                    jsonObject
                        .optString("artist")
                        .trim()
                        .takeIf {
                            it.isNotBlank()
                        }

                val album =
                    jsonObject
                        .optString("album")
                        .trim()
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
                        .trim()
                        .takeIf {
                            it.isNotBlank()
                        }

                val mediaSource =
                    MediaSource(
                        title = title,
                        uri = uri,
                        artist = artist,
                        album = album,
                        duration = duration,
                        coverArt = coverArt
                    )

                Log.d(
                    "SYNAPSE_MEDIA",
                    "PARSED MEDIA: $mediaSource"
                )

                return mediaSource
            }

            // =====================================================
            // 6. BEST MATCH FROM BACKEND
            // =====================================================

            val media =
                json.optJSONObject("media")
                    ?: run {

                        Log.e(
                            "SYNAPSE_MEDIA",
                            "MEDIA OBJECT MISSING"
                        )

                        return@withContext null
                    }

            Log.d(
                "SYNAPSE_MEDIA",
                "MEDIA OBJECT FOUND: $media"
            )

            val initialBestMatch =
                parseMedia(media)
                    ?: run {

                        Log.e(
                            "SYNAPSE_MEDIA",
                            "FAILED TO PARSE BEST MATCH"
                        )

                        return@withContext null
                    }

            // =====================================================
            // 7. PARSE ALTERNATIVE RESULTS
            // =====================================================

            val allResults =
                mutableListOf<MediaSource>()

            allResults.add(
                initialBestMatch
            )

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

                    // Avoid duplicates
                    if (
                        allResults.none {
                            it.uri == result.uri
                        }
                    ) {

                        allResults.add(result)
                    }
                }
            }

            Log.d(
                "SYNAPSE_MEDIA",
                "TOTAL RESULTS: ${allResults.size}"
            )

            // =====================================================
            // 8. USER QUERY PARSING
            // =====================================================

            val normalizedQuery =
                query
                    .lowercase()
                    .trim()

            val hasBy =
                normalizedQuery.contains(" by ")

            val artistConstraint =
                if (hasBy) {

                    normalizedQuery
                        .substringAfter(" by ")
                        .trim()

                } else {

                    ""
                }

            val songConstraint =
                if (hasBy) {

                    normalizedQuery
                        .substringBefore(" by ")
                        .trim()

                } else {

                    normalizedQuery
                }

            Log.d(
                "SYNAPSE_MEDIA",
                "SONG CONSTRAINT: $songConstraint"
            )

            Log.d(
                "SYNAPSE_MEDIA",
                "ARTIST CONSTRAINT: $artistConstraint"
            )

            // =====================================================
            // 9. RANK RESULTS
            // =====================================================

            val bestMatch =
                if (artistConstraint.isNotBlank()) {

                    allResults
                        .map { result ->

                            val title =
                                result.title
                                    .lowercase()
                                    .trim()

                            val artist =
                                result.artist
                                    ?.lowercase()
                                    ?.trim()
                                    ?: ""

                            var score = 0

                            // -------------------------------
                            // Exact title
                            // -------------------------------

                            if (
                                title == songConstraint
                            ) {

                                score += 500

                            } else if (
                                title.contains(songConstraint)
                            ) {

                                score += 250
                            }

                            // -------------------------------
                            // Exact artist
                            // -------------------------------

                            if (
                                artist == artistConstraint
                            ) {

                                score += 1000

                            } else if (
                                artist.contains(artistConstraint)
                            ) {

                                score += 700
                            }

                            // -------------------------------
                            // Artist inside title
                            // -------------------------------

                            if (
                                title.contains(artistConstraint)
                            ) {

                                score += 100
                            }

                            result to score
                        }
                        .sortedByDescending {
                            it.second
                        }
                        .also { ranked ->

                            Log.d(
                                "SYNAPSE_MEDIA",
                                "ANDROID RANKING:"
                            )

                            ranked
                                .take(10)
                                .forEachIndexed {
                                        index,
                                        pair ->

                                    Log.d(
                                        "SYNAPSE_MEDIA",
                                        "${index + 1}. " +
                                                "${pair.first.title} | " +
                                                "${pair.first.artist} | " +
                                                "score=${pair.second}"
                                    )
                                }
                        }
                        .firstOrNull()
                        ?.first

                } else {

                    initialBestMatch
                }

                    ?: run {

                        Log.e(
                            "SYNAPSE_MEDIA",
                            "NO BEST MATCH FOUND"
                        )

                        return@withContext null
                    }

            // =====================================================
            // 10. ALTERNATIVES
            // =====================================================

            val alternatives =
                allResults.filter {
                    it.uri != bestMatch.uri
                }

            Log.d(
                "SYNAPSE_MEDIA",
                "BEST MATCH: ${bestMatch.title}"
            )

            Log.d(
                "SYNAPSE_MEDIA",
                "BEST URI: ${bestMatch.uri}"
            )

            Log.d(
                "SYNAPSE_MEDIA",
                "BEST ARTIST: ${bestMatch.artist}"
            )

            Log.d(
                "SYNAPSE_MEDIA",
                "ALTERNATIVES FOUND: ${alternatives.size}"
            )

            // =====================================================
            // 11. FINAL RESULT
            // =====================================================

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

            return@withContext null

        } finally {

            connection?.disconnect()
        }
    }
}