package com.synapse.mobile.features.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SpecialAccessScreen(
    navController: NavController
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Grant Special Permissions",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                context.startActivity(
                    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                )
            }
        ) {
            Text("Accessibility")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                context.startActivity(
                    Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                )
            }
        ) {
            Text("Notification Access")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                )
                context.startActivity(intent)
            }
        ) {
            Text("Overlay Permission")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                context.startActivity(
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                )
            }
        ) {
            Text("Usage Access")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    val powerManager =
                        context.getSystemService(PowerManager::class.java)

                    if (!powerManager.isIgnoringBatteryOptimizations(context.packageName)) {

                        val intent = Intent(
                            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                            Uri.parse("package:${context.packageName}")
                        )

                        context.startActivity(intent)
                    }
                }

            }
        ) {
            Text("Battery Optimization")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate("finish") {
                    popUpTo("special_access") {
                        inclusive = true
                    }
                }
            }
        ) {
            Text("Continue")
        }

    }

}