package com.synapse.mobile.features.skills.media.session

import com.synapse.mobile.features.skills.media.resolver.MediaSource

class MediaSelectionState {

    private var pendingResults: List<MediaSource> =
        emptyList()

    fun setResults(
        results: List<MediaSource>
    ) {
        pendingResults = results
    }

    fun getResults(): List<MediaSource> {
        return pendingResults
    }

    fun getByIndex(
        index: Int
    ): MediaSource? {
        return pendingResults.getOrNull(index)
    }

    fun hasPendingSelection(): Boolean {
        return pendingResults.isNotEmpty()
    }

    fun clear() {
        pendingResults = emptyList()
    }

    fun takeByIndex(index: Int): MediaSource? {

        val result =
            pendingResults.getOrNull(index)

        if (result != null) {
            clear()
        }

        return result
    }
}