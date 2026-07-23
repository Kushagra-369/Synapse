package com.synapse.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.synapse.mobile.core.container.AppContainer
import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.navigation.AppNavigation
import com.synapse.mobile.ui.theme.SynapseTheme
import android.content.Intent
import android.net.Uri
import android.provider.Settings
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()


        val container = AppContainer(this)
        val engine = SynapseEngine(container.skillRegistry)

        if (!Settings.System.canWrite(this)) {

            val intent = Intent(
                Settings.ACTION_MANAGE_WRITE_SETTINGS,
                Uri.parse("package:$packageName")
            )

            startActivity(intent)

        }

        setContent {
            SynapseTheme {
                AppNavigation(engine)
            }
        }
    }
}