package com.synapse.mobile.features.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun PermissionScreen(
    navController: NavController
) {

    val permissions = buildList {

        add(Manifest.permission.READ_CONTACTS)

        add(Manifest.permission.CALL_PHONE)

        // Agar SMS feature abhi use nahi kar rahe ho to ise hata sakte ho.
        add(Manifest.permission.SEND_SMS)

        // Calendar
        add(Manifest.permission.READ_CALENDAR)
        add(Manifest.permission.WRITE_CALENDAR)

        add(Manifest.permission.RECORD_AUDIO)

        add(Manifest.permission.CAMERA)
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->

            val allGranted = permissions.all {
                result[it] == true
            }

            if (allGranted) {
                navController.navigate("special_access") {
                    popUpTo("permissions") {
                        inclusive = true
                    }
                }
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Grant Runtime Permissions",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = {
                launcher.launch(
                    permissions.toTypedArray()
                )
            }
        ) {
            Text("Grant Permissions")
        }

    }
}