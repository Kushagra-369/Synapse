package com.synapse.mobile.features.developer

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.ai.AIProcessor
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import com.synapse.mobile.features.nlp.NLPProcessor
import com.synapse.mobile.features.voice.*

@Composable
fun DeveloperScreen(
    navController: NavController,
    engine: SynapseEngine
) {

    var prompt by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<CommandResult?>(null) }
    var debugInfo by remember { mutableStateOf<DebugInfo?>(null) }

    val scope = rememberCoroutineScope()
    val appContext = LocalContext.current

    val ttsManager = remember { TextToSpeechManager(appContext) }

    val voiceController = remember {
        VoiceController(
            speechRecognizerManager = SpeechRecognizerManager(appContext),
            voiceProcessor = VoiceProcessor(
                nlpProcessor = NLPProcessor(),
                engine = engine,
                textToSpeechManager = ttsManager
            )
        )
    }

    val microphonePermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                result = CommandResult(
                    success = false,
                    message = "Microphone permission denied."
                )
            }
        }

    // Function to process command with debug info
    suspend fun processWithDebug(input: String): Pair<CommandResult?, DebugInfo?> {
        return try {
            val nlpProcessor = NLPProcessor()
            val command = nlpProcessor.process(input)

            android.util.Log.d("SYNAPSE", "🔍 Command: ${command.skill}/${command.action}")
            android.util.Log.d("SYNAPSE", "📦 Parameters: ${command.parameters}")

            val commandResult = engine.execute(command)

            android.util.Log.d("SYNAPSE", "✅ Result: ${commandResult.success} - ${commandResult.message}")

            val debug = DebugInfo(
                normalizedText = input,
                intent = command.parameters["intent"]?.toString() ?: "UNKNOWN",
                entities = command.parameters.filterKeys { it != "intent" },
                command = command
            )
            Pair(commandResult, debug)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(
                CommandResult(success = false, message = "Error: ${e.message}"),
                DebugInfo(
                    normalizedText = input,
                    intent = "ERROR",
                    entities = emptyMap(),
                    command = null,
                    error = e.message
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Developer Mode",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Ask Synapse") },
            placeholder = { Text("Example: Call Rahul") }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
            onClick = {
                if (ContextCompat.checkSelfPermission(
                        appContext,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    return@Button
                }

                if (prompt.isBlank()) {
                    result = CommandResult(success = false, message = "Please enter a command.")
                    return@Button
                }

                scope.launch {
                    loading = true
                    val (commandResult, debug) = processWithDebug(prompt)
                    result = commandResult
                    debugInfo = debug
                    loading = false
                }
            }
        ) {
            if (loading) CircularProgressIndicator() else Text("Execute")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (ContextCompat.checkSelfPermission(
                        appContext,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    return@Button
                }

                loading = true
                voiceController.startListening(
                    onResult = { commandResult ->
                        loading = false
                        result = commandResult
                        debugInfo = null
                    },
                    onError = {
                        result = CommandResult(false, it)
                        loading = false
                    }
                )
            }
        ) {
            Icon(Icons.Default.Mic, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Speak")
        }

        // Result Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Result", style = MaterialTheme.typography.titleMedium)
                result?.let {
                    Text("Success: ${it.success}")
                    Text(it.message)
                }
            }
        }

        // Debug Card
        if (debugInfo != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("🔍 Debug Info", style = MaterialTheme.typography.titleMedium)
                    debugInfo?.let { info ->
                        Text("Normalized: ${info.normalizedText}")
                        Text("Intent: ${info.intent}")
                        Text("Entities: ${info.entities}")
                        Text("Command: ${info.command}")
                        info.error?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error) }
                    }
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                voiceController.destroy()
                ttsManager.shutdown()
            }
        }
    }
}

// Simple data class for debug info
data class DebugInfo(
    val normalizedText: String,
    val intent: String,
    val entities: Map<String, Any>,
    val command: Command?,
    val error: String? = null
)