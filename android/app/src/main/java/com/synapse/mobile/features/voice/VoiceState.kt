package com.synapse.mobile.features.voice

enum class VoiceState {

    /**
     * Voice system is idle.
     */
    IDLE,

    /**
     * Waiting for the user to start speaking.
     */
    LISTENING,

    /**
     * User is currently speaking.
     */
    RECORDING,

    /**
     * Speech is being converted to text.
     */
    PROCESSING,

    /**
     * Synapse is speaking the response.
     */
    SPEAKING,

    /**
     * Voice interaction completed successfully.
     */
    COMPLETED,

    /**
     * An error occurred.
     */
    ERROR

}