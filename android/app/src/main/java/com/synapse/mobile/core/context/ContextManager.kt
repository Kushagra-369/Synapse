package com.synapse.mobile.core.context

import com.synapse.mobile.core.models.Command
import com.synapse.mobile.features.nlp.IntentType
import java.time.Instant

/**
 * Manages the current conversation context.
 *
 * This is the primary interface for the rest of the app to interact with the conversation state.
 * It maintains an immutable [ConversationContext] and provides methods to update it safely.
 *
 * Examples:
 * ```
 * User: "Open Chrome"
 * → ContextManager.update(command)  // lastApp = Chrome
 *
 * User: "Close it"
 * → context.resolvePronoun("it")  // returns "Chrome"
 *
 * User: "Call Rahul"
 * → lastContact = "Rahul"
 *
 * User: "Message him"
 * → resolvePronoun("him")  // returns "Rahul"
 * ```
 */
class ContextManager {

    // The current context is immutable; we replace it on every update.
    private var context: ConversationContext = ConversationContext()

    // ---- Public API ----

    /**
     * Returns the current immutable context.
     */
    fun getContext(): ConversationContext = context

    /**
     * Resets the entire context.
     */
    fun clear() {
        context = ConversationContext()
    }

    /**
     * Updates the context with a newly executed command.
     *
     * Extracts:
     * - Intent (from parameters["intent"])
     * - App, contact, website, query, location, file from parameters
     * - Skill and action from the command itself
     * - Any other parameters go into extras
     */
    fun update(command: Command) {
        // Extract intent from parameters.
        val intent = command.parameters["intent"] as? IntentType ?: IntentType.UNKNOWN

        // Copy parameters to a mutable map, but remove special keys we handle separately.
        val params = command.parameters.toMutableMap()
        params.remove("intent")  // we already have it

        // We also need to pass skill and action from the command.
        // Note: command.skill and command.action might be empty for UNKNOWN.
        context = context.updateFromCommand(
            intent = intent,
            parameters = params,
            skill = command.skill.takeIf { it.isNotEmpty() },
            action = command.action.takeIf { it.isNotEmpty() }
        )
    }

    // ---- Convenience accessors ----

    fun getLastApp(): String? = context.lastApp
    fun getLastContact(): String? = context.lastContact
    fun getLastWebsite(): String? = context.lastWebsite
    fun getLastSearchQuery(): String? = context.lastSearchQuery
    fun getLastLocation(): String? = context.lastLocation
    fun getLastFile(): String? = context.lastFile
    fun getLastIntent(): IntentType? = context.lastIntent
    fun getLastSkill(): String? = context.lastSkill
    fun getLastAction(): String? = context.lastAction

    // ---- Extras management (immutable) ----

    /**
     * Stores an extra value in the context.
     * Creates a new context with the updated extras map.
     */
    fun putExtra(key: String, value: Any) {
        context = context.copy(extras = context.extras + (key to value))
    }

    /**
     * Retrieves an extra value, or null if not present.
     */
    fun getExtra(key: String): Any? = context.extras[key]

    /**
     * Removes an extra value, returning the previous value if present.
     * @return the value that was removed, or null if it didn't exist.
     */
    fun removeExtra(key: String): Any? {
        val oldValue = context.extras[key]
        if (oldValue != null) {
            context = context.copy(extras = context.extras - key)
        }
        return oldValue
    }

    // ---- Pronoun resolution (delegates to context) ----

    /**
     * Resolves a pronoun like "it", "him", "her", "them", "there" to the most recent relevant entity.
     * Uses the improved [ConversationContext.resolvePlaceholder] method which considers history.
     */
    fun resolvePronoun(word: String): String? {
        return context.resolvePlaceholder(word)
    }

    /**
     * Finds a contact by name prefix or returns the most recent contact.
     */
    fun findContact(namePrefix: String? = null): String? {
        return context.findContact(namePrefix)
    }

    // ---- Debugging ----

    override fun toString(): String = context.toDebugString()
}