package com.example.estiamapp.ui.components

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.estiamapp.R
import kotlinx.coroutines.launch

@Composable
fun AppToastButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Button(
        onClick = {
            Toast.makeText(
                context,
                context.getString(R.string.notification_message),
                Toast.LENGTH_SHORT
            ).show()
        },
        modifier = modifier
    ) {
        Text(stringResource(R.string.toast_button))
    }
}

@Composable
fun AppSnackbarButton(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Button(
        onClick = {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_message),
                    actionLabel = context.getString(R.string.snackbar_action)
                )
            }
        },
        modifier = modifier
    ) {
        Text(stringResource(R.string.snackbar_button))
    }
}