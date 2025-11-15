package com.example.estiamapp.ui.screens

import android.Manifest
import android.os.Build
import androidx.annotation.StringRes
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.estiamapp.R
import com.example.estiamapp.notifications.NotificationHelper
import com.example.estiamapp.ui.auth.AuthViewModel
import com.example.estiamapp.ui.components.AppTopBar
import com.example.estiamapp.ui.theme.EstiamAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = AppDestination.fromRoute(backStackEntry?.destination?.route)
    val bottomDestinations = remember {
        listOf(AppDestination.Home, AppDestination.Products, AppDestination.Users, AppDestination.DbUsers)
    }

    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(id = currentDestination.titleRes),
                onSettingsClick = {
                    navController.navigate(AppDestination.Settings.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                bottomDestinations.forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination.route == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = stringResource(id = destination.labelRes)
                            )
                        },
                        label = { Text(text = stringResource(id = destination.labelRes)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestination.Home.route) {
                HomeScreen(
                    userEmail = authState.userEmail,
                    snackbarHostState = snackbarHostState
                )
            }

            composable(AppDestination.Products.route) {
                ProductsScreen()
            }

            composable(AppDestination.Users.route) {
                UsersScreen()
            }

            composable(AppDestination.DbUsers.route) {
                UsersDbScreen()
            }

            composable(AppDestination.Settings.route) {
                SettingsScreen(
                    userEmail = authState.userEmail,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(AppDestination.Home.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
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

private sealed class AppDestination(
    val route: String,
    @StringRes val titleRes: Int,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    data object Home : AppDestination(
        route = "home",
        titleRes = R.string.main_title,
        labelRes = R.string.main_tab,
        icon = Icons.Filled.Home
    )

    data object Products : AppDestination(
        route = "products",
        titleRes = R.string.products_title,
        labelRes = R.string.products_tab,
        icon = Icons.Filled.ShoppingCart
    )

    data object Users : AppDestination(
        route = "users",
        titleRes = R.string.users_title,
        labelRes = R.string.users_tab,
        icon = Icons.Filled.Group
    )

    data object DbUsers : AppDestination(
        route = "dbUsers",
        titleRes = R.string.users_db_title,
        labelRes = R.string.users_db_tab,
        icon = Icons.Filled.Group
    )

    data object Settings : AppDestination(
        route = "settings",
        titleRes = R.string.settings_title,
        labelRes = R.string.settings_title,
        icon = Icons.Filled.Settings
    )

    companion object {
        fun fromRoute(route: String?): AppDestination = when (route) {
            Home.route -> Home
            Products.route -> Products
            Users.route -> Users
            DbUsers.route -> DbUsers
            Settings.route -> Settings
            else -> Home
        }
    }
}