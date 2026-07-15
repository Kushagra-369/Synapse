package com.synapse.mobile.core.models

data class Command(
    val skill: String,
    val action: String,
    val parameters: Map<String, Any>
)