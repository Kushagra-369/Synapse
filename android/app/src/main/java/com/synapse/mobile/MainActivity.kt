package com.synapse.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.synapse.mobile.ui.theme.SynapseTheme
import android.util.Log
import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.core.models.Command

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {

            Log.d("Synapse", "1")

            enableEdgeToEdge()

            val engine = SynapseEngine()

            Log.d("Synapse", "2")

            val result = engine.execute(
                Command(
                    skill = "clock",
                    action = "set_alarm",
                    parameters = mapOf(
                        "time" to "06:00"
                    )
                )
            )

            Log.d("Synapse", "3")
            Log.d("Synapse", result.message)
            Log.d("Synapse", "4")

        } catch (e: Exception) {

            Log.e("Synapse", "CRASH", e)

        }

        setContent {
            SynapseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SynapseTheme {
        Greeting("Android")
    }
}