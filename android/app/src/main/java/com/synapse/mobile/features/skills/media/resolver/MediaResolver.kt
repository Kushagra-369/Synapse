package com.synapse.mobile.features.skills.media.resolver

import android.util.Log
import com.synapse.mobile.features.skills.media.provider.MediaProvider

class MediaResolver(
    private val providers: List<MediaProvider>
) {

    /**
     * Existing method.
     * Returns only the best match.
     */
    suspend fun resolve(
        query: String
    ): MediaSource? {

        val result =
            resolveDetailed(query)

        return result?.bestMatch
    }

    /**
     * Detailed resolution.
     * Returns best match + alternative matches.
     */
    suspend fun resolveDetailed(
        query: String
    ): MediaSearchResult? {

        Log.d(
            "SYNAPSE_MEDIA",
            "RESOLVER CALLED: query=$query"
        )

        val normalized =
            query.trim()

        if (normalized.isBlank()) {
            return null
        }

        for (provider in providers) {

            Log.d(
                "SYNAPSE_MEDIA",
                "TRYING PROVIDER: ${provider::class.simpleName}"
            )

            val result =
                provider.search(normalized)

            Log.d(
                "SYNAPSE_MEDIA",
                "SEARCH RESULT: $result"
            )

            if (result != null) {

                Log.d(
                    "SYNAPSE_MEDIA",
                    "PROVIDER FOUND: ${provider::class.simpleName}"
                )

                Log.d(
                    "SYNAPSE_MEDIA",
                    "BEST MATCH: ${result.bestMatch.title}"
                )

                Log.d(
                    "SYNAPSE_MEDIA",
                    "ALTERNATIVES: ${result.alternatives.size}"
                )

                return result
            }
        }

        Log.d(
            "SYNAPSE_MEDIA",
            "NO PROVIDER FOUND"
        )

        return null
    }
}