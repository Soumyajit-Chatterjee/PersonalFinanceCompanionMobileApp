package com.example.personalfinancecompanionmobileapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.personalfinancecompanionmobileapp.ui.FinanceViewModel
import com.example.personalfinancecompanionmobileapp.ui.screens.goal.GoalScreen
import com.example.personalfinancecompanionmobileapp.ui.screens.home.HomeScreen
import com.example.personalfinancecompanionmobileapp.ui.screens.insights.InsightsScreen
import com.example.personalfinancecompanionmobileapp.ui.screens.transactions.AddEditTransactionScreen
import com.example.personalfinancecompanionmobileapp.ui.screens.transactions.TransactionListScreen
import androidx.compose.ui.graphics.Color
import com.example.personalfinancecompanionmobileapp.ui.theme.EarthForest
import com.example.personalfinancecompanionmobileapp.ui.theme.EarthCream
import com.example.personalfinancecompanionmobileapp.ui.theme.EarthOlive
import com.example.personalfinancecompanionmobileapp.ui.screens.SettingsScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Transactions : Screen("transactions", "Transactions", Icons.Default.List)
    object AddTransaction : Screen("add_transaction", "Add", Icons.Default.List) // Not in BottomBar
    object Insights : Screen("insights", "Insights", Icons.Default.Insights)
    object Goal : Screen("goal", "Goal", Icons.Default.TrackChanges)
    object Settings : Screen("settings", "Settings", Icons.Default.List) // Not in BottomBar
}

val items = listOf(
    Screen.Home,
    Screen.Transactions,
    Screen.Insights,
    Screen.Goal
)

@Composable
fun FinanceApp(viewModel: FinanceViewModel) {
    val navController = rememberNavController()
    
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            
            // Only show bottom bar on main screens
            if (currentRoute in items.map { it.route }) {
                NavigationBar(
                    containerColor = EarthForest,
                    contentColor = EarthCream
                ) {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EarthForest,
                                unselectedIconColor = EarthCream.copy(alpha = 0.7f),
                                selectedTextColor = EarthCream,
                                unselectedTextColor = EarthCream.copy(alpha = 0.7f),
                                indicatorColor = EarthOlive
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(300)) { it / 2 } },
            exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(animationSpec = tween(300)) { -it / 2 } },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(300)) { -it / 2 } },
            popExitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(animationSpec = tween(300)) { it / 2 } }
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToTransactions = {
                        navController.navigate(Screen.Transactions.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    }
                )
            }
            composable(Screen.Transactions.route) {
                TransactionListScreen(
                    viewModel = viewModel,
                    onAddTransaction = { navController.navigate(Screen.AddTransaction.route) },
                    onEditTransaction = { id -> navController.navigate(Screen.AddTransaction.route + "?transactionId=$id") }
                )
            }
            composable(
                route = Screen.AddTransaction.route + "?transactionId={transactionId}",
                arguments = listOf(navArgument("transactionId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId")
                AddEditTransactionScreen(
                    viewModel = viewModel,
                    transactionId = transactionId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Insights.route) {
                InsightsScreen(viewModel = viewModel)
            }
            composable(Screen.Goal.route) {
                GoalScreen(viewModel = viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
