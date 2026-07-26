package com.synapse.mobile.features.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale

class SpeechRecognizerManager(
    private val context: Context
) {

    private var speechRecognizer: SpeechRecognizer? = null

    /**
     * Starts one-shot speech recognition.
     */
    fun startListening(
        onResult: (String) -> Unit,
        onError: (String) -> Unit
    ) {

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError("Speech recognition is not available on this device.")
            return
        }

        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        }

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {

                val message = when (error) {

                    SpeechRecognizer.ERROR_AUDIO ->
                        "Audio recording error."

                    SpeechRecognizer.ERROR_CLIENT ->
                        "Client error."

                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                        "Microphone permission denied."

                    SpeechRecognizer.ERROR_NETWORK ->
                        "Network error."

                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                        "Network timeout."

                    SpeechRecognizer.ERROR_NO_MATCH ->
                        "No speech recognized."

                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                        "Speech recognizer is busy."

                    SpeechRecognizer.ERROR_SERVER ->
                        "Speech server error."

                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                        "No speech detected."

                    else ->
                        "Speech recognition failed."
                }

                onError(message)
            }

            override fun onResults(results: Bundle?) {

                val matches = results?.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )

                if (!matches.isNullOrEmpty()) {
                    onResult(matches.first())
                } else {
                    onError("No speech recognized.")
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(
                eventType: Int,
                params: Bundle?
            ) {
            }

        })

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )

            putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                false
            )

            putExtra(
                RecognizerIntent.EXTRA_MAX_RESULTS,
                1
            )
        }

        speechRecognizer?.startListening(intent)
    }

    /**
     * Stops listening.
     */
    fun stopListening() {
        speechRecognizer?.stopListening()
    }

    /**
     * Cancels current recognition.
     */
    fun cancel() {
        speechRecognizer?.cancel()
    }

    /**
     * Releases resources.
     */
    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
}