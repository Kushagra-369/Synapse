package com.synapse.mobile.features.developer
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


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
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
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

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_brightness",
                        parameters = mapOf(
                            "percentage" to 25
                        )
                    )

                )

            }
        ) {

            Text("Brightness 25%")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_brightness",
                        parameters = mapOf(
                            "percentage" to 50
                        )
                    )

                )

            }
        ) {

            Text("Brightness 50%")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_brightness",
                        parameters = mapOf(
                            "percentage" to 100
                        )
                    )

                )

            }
        ) {

            Text("Brightness 100%")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_volume",
                        parameters = mapOf(
                            "percentage" to 25
                        )
                    )

                )

            }
        ) {

            Text("Volume 25%")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_volume",
                        parameters = mapOf(
                            "percentage" to 50
                        )
                    )

                )

            }
        ) {

            Text("Volume 50%")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_volume",
                        parameters = mapOf(
                            "percentage" to 100
                        )
                    )

                )

            }
        ) {

            Text("Volume 100%")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_wifi",
                        parameters = mapOf(
                            "enabled" to true
                        )
                    )

                )

            }
        ) {

            Text("WiFi ON")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_wifi",
                        parameters = mapOf(
                            "enabled" to false
                        )
                    )

                )

            }
        ) {

            Text("WiFi OFF")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_bluetooth",
                        parameters = mapOf(
                            "enabled" to true
                        )
                    )

                )

            }
        ) {

            Text("Bluetooth ON")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_bluetooth",
                        parameters = mapOf(
                            "enabled" to false
                        )
                    )

                )

            }
        ) {

            Text("Bluetooth OFF")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_rotation",
                        parameters = mapOf(
                            "enabled" to true
                        )
                    )

                )

            }
        ) {

            Text("Rotation ON")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_rotation",
                        parameters = mapOf(
                            "enabled" to false
                        )
                    )

                )

            }
        ) {

            Text("Rotation OFF")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_dnd",
                        parameters = mapOf(
                            "enabled" to true
                        )
                    )

                )

            }
        ) {

            Text("DND ON")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "set_dnd",
                        parameters = mapOf(
                            "enabled" to false
                        )
                    )

                )

            }
        ) {

            Text("DND OFF")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "device",
                        action = "get_battery",
                        parameters = emptyMap()
                    )

                )

            }
        ) {

            Text("Battery Status")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "whatsapp",
                        action = "open",
                        parameters = emptyMap()
                    )

                )

            }
        ) {

            Text("Open WhatsApp")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "whatsapp",
                        action = "send_message",
                        parameters = mapOf(

                            "phone" to "919215808489",

                            "message" to "Hello from Synapse"

                        )
                    )

                )

            }
        ) {

            Text("Send Message")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "whatsapp",
                        action = "open_chat",
                        parameters = mapOf(

                            "phone" to "919215808489"

                        )
                    )

                )

            }
        ) {

            Text("Open Chat")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "whatsapp",
                        action = "voice_call",
                        parameters = mapOf(

                            "phone" to "919215808489"

                        )
                    )

                )

            }
        ) {

            Text("Voice Call")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "whatsapp",
                        action = "video_call",
                        parameters = mapOf(

                            "phone" to "919215808489"

                        )
                    )

                )

            }
        ) {

            Text("Video Call")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "youtube",
                        action = "open",
                        parameters = emptyMap()
                    )

                )

            }
        ) {

            Text("Open YouTube")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "youtube",
                        action = "search",
                        parameters = mapOf(
                            "query" to "shadow king dost"
                        )
                    )

                )

            }
        ) {

            Text("Search YouTube")

        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(

                    Command(
                        skill = "youtube",
                        action = "play",
                        parameters = mapOf(
                            "query" to "Interstellar Theme"
                        )
                    )

                )

            }
        ) {

            Text("Play Video")

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
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(
                    Command(
                        skill = "maps",
                        action = "open_maps",
                        parameters = mapOf(
                            "query" to "India Gate Delhi"
                        )
                    )
                )

            }
        ) {
            Text("Open Maps")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(
                    Command(
                        skill = "maps",
                        action = "search_place",
                        parameters = mapOf(
                            "place" to "Red Fort Delhi"
                        )
                    )
                )

            }
        ) {
            Text("Search Place")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(
                    Command(
                        skill = "maps",
                        action = "navigate",
                        parameters = mapOf(
                            "destination" to "Connaught Place Delhi"
                        )
                    )
                )

            }
        ) {
            Text("Navigate")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                result = engine.execute(
                    Command(
                        skill = "maps",
                        action = "get_current_location",
                        parameters = emptyMap()
                    )
                )

            }
        ) {
            Text("Current Location")
        }



        Button(
            onClick = {

                result = engine.execute(
                    Command(
                        skill = "flashlight",
                        action = "toggle",
                        parameters = emptyMap()
                    )
                )

            }
        ) {

            Text("Toggle Flashlight")

        }

        Button(
            onClick = {
                result = engine.execute(
                    Command(
                        skill = "gallery",
                        action = "open_gallery",
                        parameters = emptyMap()
                    )
                )
            }
        ) {
            Text("Open Gallery")
        }

        Button(
            onClick = {
                result = engine.execute(
                    Command(
                        skill = "gallery",
                        action = "open_photos",
                        parameters = emptyMap()
                    )
                )
            }
        ) {
            Text("Open Photos")
        }

        Button(
            onClick = {
                result = engine.execute(
                    Command(
                        skill = "gallery",
                        action = "open_videos",
                        parameters = emptyMap()
                    )
                )
            }
        ) {
            Text("Open Videos")
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