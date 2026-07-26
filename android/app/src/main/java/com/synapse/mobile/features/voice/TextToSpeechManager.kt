package com.synapse.mobile.features.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechManager(
    context: Context
) : TextToSpeech.OnInitListener {

    private var initialized = false

    private val textToSpeech = TextToSpeech(
        context.applicationContext,
        this
    )

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {

            val result = textToSpeech.setLanguage(
                Locale.getDefault()
            )

            initialized =
                result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED

        }

    }

    /**
     * Speaks the given text.
     */
    fun speak(text: String) {

        if (!initialized) return

        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "synapse_tts"
        )

    }

    /**
     * Speaks while keeping previous utterances.
     */
    fun speakQueued(text: String) {

        if (!initialized) return

        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_ADD,
            null,
            "synapse_tts_queue"
        )

    }

    /**
     * Stops current speech.
     */
    fun stop() {
        textToSpeech.stop()
    }

    /**
     * Whether TTS is currently speaking.
     */
    fun isSpeaking(): Boolean {
        return textToSpeech.isSpeaking
    }

    /**
     * Sets speech rate.
     * 1.0 = normal
     */
    fun setSpeechRate(rate: Float) {
        textToSpeech.setSpeechRate(rate)
    }

    /**
     * Sets voice pitch.
     * 1.0 = normal
     */
    fun setPitch(pitch: Float) {
        textToSpeech.setPitch(pitch)
    }

    /**
     * Changes language.
     */
    fun setLanguage(locale: Locale): Boolean {

        val result = textToSpeech.setLanguage(locale)

        return result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED

    }

    /**
     * Releases resources.
     */
    fun shutdown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

}