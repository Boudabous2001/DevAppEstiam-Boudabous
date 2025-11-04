package com.example.estiamapp.ui.screens

import android.Manifest
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.estiamapp.Greeting
import com.example.estiamapp.notifications.NotificationHelper
import com.example.estiamapp.ui.components.AppSnackbarButton
import com.example.estiamapp.ui.components.AppToastButton
import com.example.estiamapp.ui.components.AppTopBar
import com.example.estiamapp.ui.components.LanguageDropdown
import com.example.estiamapp.ui.theme.EstiamAppTheme
import com.example.estiamapp.work.enqueueOneTimeAfter10Sec
import com.example.estiamapp.work.enqueueOneTimeWifiCharging

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen () {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppTopBar(title = "ESTIAM") },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            val ctx = LocalContext.current

            Greeting(
                name = Build.BRAND,
                modifier = Modifier.padding(innerPadding)
            )

            AppToastButton(
                label = "Display Toast",
                message = "Toast works!!!"
            )

            AppSnackbarButton(
                snackbarHostState = snackbarHostState,
                label = "Display Snackbar",
                message = "Snackbar works!!",
                actionLabel = "Confirm"
            )

            NotificationSection()

            LanguageDropdown()

            Button(
                onClick = { enqueueOneTimeAfter10Sec(ctx) }
            ) {
                Text("One Time Action - 10 sec")
            }

            Button(
                onClick = { enqueueOneTimeWifiCharging(ctx) }
            ) {
                Text("Action - Wifi & Charging")
            }
        }
    }
}

@Composable
fun NotificationSection() {
    val context = LocalContext.current

    var hasPermission by remember {
        mutableStateOf(
            Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PermissionChecker.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        granted -> hasPermission = granted
    }

    Button(
        onClick = {
            if (Build.VERSION.SDK_INT >= 33 && !hasPermission) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                NotificationHelper.show(
                    context,
                    title = "Test event",
                    message = "This is a notification"
                )
            }
        }
    ) {
        Text("Send notification")
    }
}

@Preview(
    showBackground = true,
    name = "Phone - Light",
    device = Devices.PHONE,
    locale = "fr"
)
@Composable
fun MainScreenPreview() {
    EstiamAppTheme {
        MainScreen()
    }
}