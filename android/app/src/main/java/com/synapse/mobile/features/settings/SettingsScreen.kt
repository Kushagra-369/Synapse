package com.synapse.mobile.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Permissions",
            style = MaterialTheme.typography.headlineMedium
        )

        PermissionStatusCard(
            permission = "Contacts",
            granted = true
        )

        PermissionStatusCard(
            permission = "Phone",
            granted = true
        )

        PermissionStatusCard(
            permission = "Calendar",
            granted = false
        )

        PermissionStatusCard(
            permission = "SMS",
            granted = false
        )

        PermissionStatusCard(
            permission = "Notifications",
            granted = false
        )

        PermissionStatusCard(
            permission = "Accessibility",
            granted = false
        )

    }

}