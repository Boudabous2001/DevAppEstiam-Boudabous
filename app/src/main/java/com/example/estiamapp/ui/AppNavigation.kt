package com.example.estiamapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.estiamapp.R
import com.example.estiamapp.ui.auth.AuthViewModel
import com.example.estiamapp.ui.auth.RequireAuth
import com.example.estiamapp.ui.components.AppTopBar
import com.example.estiamapp.ui.screens.*

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Products : Screen("products")
    data object Users : Screen("users")
    data object UsersDb : Screen("users_db")
    data object Settings : Screen("settings")
}

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val labelRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, Icons.Default.Home, R.string.main_tab),
    BottomNavItem(Screen.Products, Icons.Default.ShoppingCart, R.string.products_tab),
    BottomNavItem(Screen.Users, Icons.Default.Person, R.string.users_tab),
    BottomNavItem(Screen.UsersDb, Icons.Default.SupervisorAccount, R.string.users_db_tab)
)

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(authState.isAuthenticated) {
        if (!authState.isAuthenticated) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authState.isAuthenticated) Screen.Home.route else Screen.Login.route
    ) {
        // Auth screens
        composable(Screen.Login.route) {
            LoginScreen(
                uiState = authState,
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onClearError = { authViewModel.clearError() }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                uiState = authState,
                onRegisterClick = { email, password ->
                    authViewModel.register(email, password)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onClearError = { authViewModel.clearError() }
            )
        }

        // Protected screens with bottom nav
        composable(Screen.Home.route) {
            RequireAuth(
                authViewModel = authViewModel,
                onNotAuthenticated = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) {
                MainScaffold(
                    navController = navController,
                    currentScreen = Screen.Home,
                    authViewModel = authViewModel
                )
            }
        }

        composable(Screen.Products.route) {
            RequireAuth(authViewModel, { navController.navigate(Screen.Login.route) }) {
                MainScaffold(navController, Screen.Products, authViewModel)
            }
        }

        composable(Screen.Users.route) {
            RequireAuth(authViewModel, { navController.navigate(Screen.Login.route) }) {
                MainScaffold(navController, Screen.Users, authViewModel)
            }
        }

        composable(Screen.UsersDb.route) {
            RequireAuth(authViewModel, { navController.navigate(Screen.Login.route) }) {
                MainScaffold(navController, Screen.UsersDb, authViewModel)
            }
        }

        composable(Screen.Settings.route) {
            RequireAuth(authViewModel, { navController.navigate(Screen.Login.route) }) {
                SettingsScreen(
                    userEmail = authState.userEmail,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun MainScaffold(
    navController: NavHostController,
    currentScreen: Screen,
    authViewModel: AuthViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            val title = when (currentScreen) {
                Screen.Home -> stringResource(R.string.main_title)
                Screen.Products -> stringResource(R.string.products_title)
                Screen.Users -> stringResource(R.string.users_title)
                Screen.UsersDb -> stringResource(R.string.users_db_title)
                else -> ""
            }
            AppTopBar(
                title = title,
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when (currentScreen) {
            Screen.Home -> HomeScreen(
                userEmail = authState.userEmail,
                snackbarHostState = snackbarHostState,
                modifier = Modifier.padding(padding)
            )
            Screen.Products -> ProductsScreen(modifier = Modifier.padding(padding))
            Screen.Users -> UsersScreen(modifier = Modifier.padding(padding))
            Screen.UsersDb -> UsersDbScreen(modifier = Modifier.padding(padding))
            else -> {}
        }
    }
}

@Composable
private fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(stringResource(item.labelRes)) },
                selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}