package com.synapse.mobile.core.context

import com.synapse.mobile.features.nlp.IntentType
import java.time.Instant

/**
 * Manages the current conversation state for Synapse.
 *
 * Supports:
 * - Storing a history of recent entities (apps, contacts, websites, etc.)
 * - Resolving follow-up references like "him", "that", "it".
 * - Time-aware context (e.g., "yesterday", "tomorrow").
 * - Type-safe extras for future extensibility.
 */
data class ConversationContext(
    // --- History stacks (most recent first) ---
    val intentHistory: List<IntentRecord> = emptyList(),
    val appHistory: List<EntityRecord> = emptyList(),
    val contactHistory: List<EntityRecord> = emptyList(),
    val websiteHistory: List<EntityRecord> = emptyList(),
    val searchHistory: List<EntityRecord> = emptyList(),
    val locationHistory: List<EntityRecord> = emptyList(),
    val fileHistory: List<EntityRecord> = emptyList(),
    val skillHistory: List<EntityRecord> = emptyList(),
    val actionHistory: List<EntityRecord> = emptyList(),

    // --- Extras (type-safe keys) ---
    val extras: Map<String, Any> = emptyMap(),

    // --- Time context ---
    val currentTime: Instant = Instant.now()
) {

    // ---- Convenience accessors (most recent) ----

    val lastIntent: IntentType? = intentHistory.firstOrNull()?.intent
    val lastApp: String? = appHistory.firstOrNull()?.value
    val lastContact: String? = contactHistory.firstOrNull()?.value
    val lastWebsite: String? = websiteHistory.firstOrNull()?.value
    val lastSearchQuery: String? = searchHistory.firstOrNull()?.value
    val lastLocation: String? = locationHistory.firstOrNull()?.value
    val lastFile: String? = fileHistory.firstOrNull()?.value
    val lastSkill: String? = skillHistory.firstOrNull()?.value
    val lastAction: String? = actionHistory.firstOrNull()?.value

    // ---- Helper to resolve missing parameters ----

    /**
     * Resolves a placeholder (e.g., "him", "it", "that") by looking up the most recent relevant entity.
     */
    fun resolvePlaceholder(placeholder: String): String? {
        return when (placeholder.lowercase()) {
            "him", "her", "them", "that person" -> lastContact
            "it", "that", "this" -> lastApp ?: lastWebsite ?: lastFile
            "there" -> lastLocation
            else -> null
        }
    }

    /**
     * Returns the most recent contact, optionally matching a name prefix.
     */
    fun findContact(namePrefix: String? = null): String? {
        if (namePrefix == null) return lastContact
        return contactHistory.firstOrNull { it.value.startsWith(namePrefix, ignoreCase = true) }?.value
    }

    // ---- Context update ----

    /**
     * Creates a new context by incorporating a newly executed command.
     *
     * @param intent The intent that was executed.
     * @param parameters The parameters from the command.
     * @param skill The skill that handled the intent.
     * @param action The action within the skill.
     * @param maxHistory Maximum number of items to keep per stack (default 5).
     * @return An updated [ConversationContext].
     */
    fun updateFromCommand(
        intent: IntentType,
        parameters: Map<String, Any>,
        skill: String? = null,
        action: String? = null,
        maxHistory: Int = 5
    ): ConversationContext {
        var ctx = this

        // Add intent record.
        ctx = ctx.copy(
            intentHistory = (ctx.intentHistory + IntentRecord(intent, Instant.now())).take(maxHistory)
        )

        // Add app if present.
        parameters["app"]?.let { app ->
            ctx = ctx.copy(
                appHistory = (ctx.appHistory + EntityRecord(app.toString(), Instant.now())).take(maxHistory)
            )
        }

        // Add contact if present.
        parameters["contact"]?.let { contact ->
            ctx = ctx.copy(
                contactHistory = (ctx.contactHistory + EntityRecord(contact.toString(), Instant.now())).take(maxHistory)
            )
        }

        // Add website if present.
        parameters["website"]?.let { website ->
            ctx = ctx.copy(
                websiteHistory = (ctx.websiteHistory + EntityRecord(website.toString(), Instant.now())).take(maxHistory)
            )
        }

        // Add search query if present.
        parameters["query"]?.let { query ->
            ctx = ctx.copy(
                searchHistory = (ctx.searchHistory + EntityRecord(query.toString(), Instant.now())).take(maxHistory)
            )
        }

        // Add location if present.
        parameters["location"]?.let { location ->
            ctx = ctx.copy(
                locationHistory = (ctx.locationHistory + EntityRecord(location.toString(), Instant.now())).take(maxHistory)
            )
        }

        // Add file if present.
        parameters["file"]?.let { file ->
            ctx = ctx.copy(
                fileHistory = (ctx.fileHistory + EntityRecord(file.toString(), Instant.now())).take(maxHistory)
            )
        }

        // Add skill and action.
        skill?.let {
            ctx = ctx.copy(
                skillHistory = (ctx.skillHistory + EntityRecord(it, Instant.now())).take(maxHistory)
            )
        }
        action?.let {
            ctx = ctx.copy(
                actionHistory = (ctx.actionHistory + EntityRecord(it, Instant.now())).take(maxHistory)
            )
        }

        // Merge extras (if any new ones).
        val newExtras = parameters.filterKeys { key ->
            key !in setOf("app", "contact", "website", "query", "location", "file")
        }
        if (newExtras.isNotEmpty()) {
            ctx = ctx.copy(extras = ctx.extras + newExtras)
        }

        return ctx
    }

    /**
     * Clears the entire context, resetting all history and extras.
     */
    fun clear(): ConversationContext = ConversationContext()

    /**
     * Returns a string summary for debugging.
     */
    fun toDebugString(): String {
        return "Context(lastIntent=$lastIntent, lastApp=$lastApp, lastContact=$lastContact, " +
                "lastWebsite=$lastWebsite, lastQuery=$lastSearchQuery, lastLocation=$lastLocation, " +
                "extras=${extras.keys}, historySizes=(${intentHistory.size}, ${appHistory.size}, ...))"
    }

    // ---- Companion with type-safe extras keys ----

    companion object {
        // Standard extra keys to avoid typos.
        const val EXTRA_WIFI = "wifi"
        const val EXTRA_BLUETOOTH = "bluetooth"
        const val EXTRA_SONG = "song"
        const val EXTRA_PLAYLIST = "playlist"
        const val EXTRA_DOCUMENT = "document"
        const val EXTRA_VOLUME = "volume"
        const val EXTRA_BRIGHTNESS = "brightness"
    }
}

// ---- Supporting data classes ----

/**
 * Records an intent with a timestamp.
 */
data class IntentRecord(
    val intent: IntentType,
    val timestamp: Instant
)

/**
 * Records any named entity (contact, app, etc.) with a timestamp.
 */
data class EntityRecord(
    val value: String,
    val timestamp: Instant
)