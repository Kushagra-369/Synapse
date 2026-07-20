package com.synapse.mobile.features.developer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import java.util.Calendar

@Composable
fun DeveloperScreen(
    navController: NavController,
    engine: SynapseEngine
) {

    var result by remember {
        mutableStateOf<CommandResult?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Developer Mode",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(
                    Command(
                        skill = "phone",
                        action = "dial_phone",
                        parameters = mapOf(
                            "number" to "9876543210"
                        )
                    )
                )

            }
        ) {
            Text("Test Phone")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                val cal = Calendar.getInstance().apply {
                    set(2026, Calendar.JULY, 20, 12, 0, 0)
                }

                val start = cal.timeInMillis

                cal.add(Calendar.HOUR_OF_DAY, 1)

                val end = cal.timeInMillis

                result = engine.execute(
                    Command(
                        skill = "calendar",
                        action = "create_event",
                        parameters = mapOf(
                            "title" to "🎉 HAPPY BDAY ME 🎂",
                            "startTime" to start,
                            "endTime" to end
                        )
                    )
                )

            }
        ) {
            Text("Test Calendar")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(
                    Command(
                        skill = "browser",
                        action = "open_url",
                        parameters = mapOf(
                            "url" to "https://www.google.com"
                        )
                    )
                )

            }
        ) {
            Text("Test Browser")
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    "Result",
                    style = MaterialTheme.typography.titleMedium
                )

                result?.let {

                    Text("Success : ${it.success}")

                    Text(it.message)

                }

            }

        }

    }

}