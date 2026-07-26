package com.synapse.mobile.features.voice

import com.synapse.mobile.core.models.CommandResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceController(
    private val speechRecognizerManager: SpeechRecognizerManager,
    private val voiceProcessor: VoiceProcessor
) {

    private var state: VoiceState = VoiceState.IDLE

    fun getState(): VoiceState = state

    fun startListening(
        onResult: (CommandResult) -> Unit,
        onError: (String) -> Unit
    ) {

        state = VoiceState.LISTENING

        speechRecognizerManager.startListening(

            onResult = { spokenText ->

                state = VoiceState.PROCESSING

                CoroutineScope(Dispatchers.Main).launch {

                    try {

                        val result = voiceProcessor.process(spokenText)

                        state = VoiceState.COMPLETED

                        onResult(result)

                    } catch (e: Exception) {

                        state = VoiceState.ERROR

                        onError(
                            e.message ?: "Unknown error."
                        )

                    }

                }

            },

            onError = { error ->

                state = VoiceState.ERROR

                onError(error)

            }

        )

    }

    fun stopListening() {

        speechRecognizerManager.stopListening()

        state = VoiceState.IDLE

    }

    fun cancelListening() {

        speechRecognizerManager.cancel()

        state = VoiceState.IDLE

    }

    fun destroy() {

        speechRecognizerManager.destroy()

        state = VoiceState.IDLE

    }

}