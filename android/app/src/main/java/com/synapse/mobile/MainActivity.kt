package com.synapse.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
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

        // ✅ FIX: Pass context as first parameter
        val container = AppContainer(this)
        val engine = SynapseEngine(
            context = this,              // ← Pass context FIRST
            registry = container.skillRegistry  // ← Then registry
        )

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1001
            )
        }

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