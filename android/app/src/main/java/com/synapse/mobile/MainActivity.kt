package com.synapse.mobile
import java.util.Calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import com.synapse.mobile.navigation.AppNavigation
import com.synapse.mobile.ui.theme.SynapseTheme
import android.util.Log
import com.synapse.mobile.core.container.AppContainer
import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.core.models.Command
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val container = AppContainer(this)
        val engine = SynapseEngine(container.skillRegistry)

        val now = System.currentTimeMillis()

        val calendar = Calendar.getInstance().apply {
            set(2026, Calendar.JULY, 20, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val startTime = calendar.timeInMillis

        calendar.set(2026, Calendar.JULY, 20, 23, 59, 59)

        val endTime = calendar.timeInMillis

        val result = engine.execute(
            Command(
                skill = "phone",
                action = "dial_phone",
                parameters = mapOf(
                    "number" to "9416468645"
                )
            )
        )

        Log.d("SYNAPSE", result.message)

        setContent {
            SynapseTheme {
                AppNavigation()
            }
        }
    }
}