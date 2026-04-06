package com.example.personalfinancecompanionmobileapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.personalfinancecompanionmobileapp.ui.FinanceViewModel
import com.example.personalfinancecompanionmobileapp.utils.ExportHelper
import android.app.Activity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: FinanceViewModel,
    onNavigateBack: () -> Unit
) {
    val currency by viewModel.currency.collectAsState()
    val themeMode by viewModel.theme.collectAsState()
    val biometricEnabled by viewModel.biometricEnabled.collectAsState()
    val notificationEnabled by viewModel.notificationEnabled.collectAsState()

    val context = LocalContext.current
    val activity = context as? Activity
    var exportResultMsg by remember { mutableStateOf<String?>(null) }
    
    val transactions by viewModel.transactions.collectAsState()

    var pendingNotificationPermissionCheck by remember { mutableStateOf(false) }

    LaunchedEffect(pendingNotificationPermissionCheck) {
        if (!pendingNotificationPermissionCheck) return@LaunchedEffect

        repeat(60) {
            val isGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

            if (isGranted) {
                viewModel.saveNotificationSettings(true, "20:00")
                ExportHelper.setupDailyReminder(context)
                pendingNotificationPermissionCheck = false
                return@LaunchedEffect
            }

            // After the permission sheet closes and focus returns, treat still-not-granted as deny.
            if (activity?.hasWindowFocus() == true && it > 2) {
                viewModel.saveNotificationSettings(false, "20:00")
                pendingNotificationPermissionCheck = false
                return@LaunchedEffect
            }

            delay(250)
        }

        if (pendingNotificationPermissionCheck) {
            viewModel.saveNotificationSettings(false, "20:00")
            pendingNotificationPermissionCheck = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Currency
            Text("Currency", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("$", "€", "£", "₹").forEach { symbol ->
                    FilterChip(
                        selected = currency == symbol,
                        onClick = { viewModel.saveCurrency(symbol) },
                        label = { Text(symbol) }
                    )
                }
            }

            Divider()

            // Theme
            Text("Theme", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("System", "Light", "Dark").forEach { type ->
                    FilterChip(
                        selected = themeMode == type,
                        onClick = { viewModel.saveTheme(type) },
                        label = { Text(type) }
                    )
                }
            }

            Divider()

            // Biometric
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Require Biometric Unlock", style = MaterialTheme.typography.titleMedium)
                Switch(
                    checked = biometricEnabled,
                    onCheckedChange = { viewModel.toggleBiometric(it) }
                )
            }
            
            Divider()
            
            // Notifications
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Daily Reminders (8:00 PM)", style = MaterialTheme.typography.titleMedium)
                Switch(
                    checked = notificationEnabled,
                    onCheckedChange = { isChecked -> 
                        if (isChecked) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val isGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                                if (isGranted) {
                                    viewModel.saveNotificationSettings(true, "20:00")
                                    ExportHelper.setupDailyReminder(context)
                                } else {
                                    if (activity != null) {
                                        pendingNotificationPermissionCheck = true
                                        ActivityCompat.requestPermissions(
                                            activity,
                                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                            100
                                        )
                                    } else {
                                        viewModel.saveNotificationSettings(false, "20:00")
                                    }
                                }
                            } else {
                                viewModel.saveNotificationSettings(true, "20:00")
                                ExportHelper.setupDailyReminder(context)
                            }
                        } else {
                            viewModel.saveNotificationSettings(false, "20:00")
                            ExportHelper.cancelDailyReminder(context)
                        }
                    }
                )
            }

            Divider()

            // Export Data
            Text("Data Backup", style = MaterialTheme.typography.titleMedium)
            Button(onClick = {
                val intent = ExportHelper.exportTransactionsToCsv(context, transactions)
                if (intent != null) {
                    context.startActivity(intent)
                    exportResultMsg = "Export Intent Launched"
                } else {
                    exportResultMsg = "Failed to create backup"
                }
            }) {
                Text("Export Data (CSV)")
            }

            if (exportResultMsg != null) {
                Text(exportResultMsg!!, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
