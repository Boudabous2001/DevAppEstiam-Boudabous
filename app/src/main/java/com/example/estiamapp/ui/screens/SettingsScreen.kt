package com.example.estiamapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.estiamapp.R
import com.example.estiamapp.ui.components.LanguageDropdown
import com.example.estiamapp.ui.theme.EstiamAppTheme

@Composable
fun SettingsScreen(
    userEmail: String?,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User info
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Compte",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = userEmail ?: "Non connecté",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Language
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Préférences",
                    style = MaterialTheme.typography.titleMedium
                )

                LanguageDropdown()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.logout))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    EstiamAppTheme {
        SettingsScreen(
            userEmail = "test@example.com",
            onLogout = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenDarkPreview() {
    EstiamAppTheme {
        SettingsScreen(
            userEmail = "test@example.com",
            onLogout = {}
        )
    }
}