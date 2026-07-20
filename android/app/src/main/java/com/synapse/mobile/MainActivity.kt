package com.synapse.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.synapse.mobile.core.container.AppContainer
import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.navigation.AppNavigation
import com.synapse.mobile.ui.theme.SynapseTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val container = AppContainer(this)
        val engine = SynapseEngine(container.skillRegistry)

        setContent {
            SynapseTheme {
                AppNavigation(engine)
            }
        }
    }
}