package com.synapse.mobile.features.developer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.ai.AIProcessor
import kotlinx.coroutines.launch

@Composable
fun DeveloperScreen(
    navController: NavController,
    engine: SynapseEngine
) {

    var prompt by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var result by remember {
        mutableStateOf<CommandResult?>(null)
    }

    val scope = rememberCoroutineScope()

    val processor = AIProcessor(engine)

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
            onValueChange = {
                prompt = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Ask Synapse")
            },
            placeholder = {
                Text("Example: Play Arijit Singh songs")
            }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
            onClick = {

                if (prompt.isBlank()) {
                    result = CommandResult(
                        false,
                        "Please enter a command."
                    )
                    return@Button
                }

                scope.launch {

                    loading = true

                    result = processor.process(prompt)

                    loading = false
                }
            }
        ) {

            if (loading) {

                CircularProgressIndicator()

            } else {

                Text("Execute")

            }

        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Result",
                    style = MaterialTheme.typography.titleMedium
                )

                result?.let {

                    Text("Success: ${it.success}")

                    Text(it.message)

                }

            }

        }

    }

}