package com.example.personalfinancecompanionmobileapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalfinancecompanionmobileapp.data.db.AppDatabase
import com.example.personalfinancecompanionmobileapp.data.repository.RoomFinanceRepository
import com.example.personalfinancecompanionmobileapp.data.repository.SettingsRepository
import com.example.personalfinancecompanionmobileapp.ui.FinanceViewModel
import com.example.personalfinancecompanionmobileapp.ui.FinanceViewModelFactory
import com.example.personalfinancecompanionmobileapp.ui.navigation.FinanceApp
import com.example.personalfinancecompanionmobileapp.ui.theme.EarthBackground
import com.example.personalfinancecompanionmobileapp.ui.theme.PersonalFinanceCompanionMobileAppTheme


class MainActivity : FragmentActivity() {

    private lateinit var settingsRepository: SettingsRepository
    private var isAuthenticated by mutableStateOf(false)
    private var requiresAuth = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        settingsRepository= SettingsRepository(this)
        val database= AppDatabase.getDatabase(this)
        val repository= RoomFinanceRepository(database.transactionDao(), database.goalDao())
        
        lifecycleScope.launchWhenCreated {
            settingsRepository.biometricEnabledFlow.collect { enabled ->
                requiresAuth = enabled
            }
        }

        
        enableEdgeToEdge()
        setContent {
            val themeMode by settingsRepository.themeFlow.collectAsState(initial = "System")
            val darkTheme = when (themeMode) {
                "Dark" -> true
                "Light" -> false
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }

            PersonalFinanceCompanionMobileAppTheme(darkTheme = darkTheme) {
                EarthBackground {
                    if (!requiresAuth || isAuthenticated) {
                        val viewModel: FinanceViewModel = viewModel(
                            factory = FinanceViewModelFactory( repository, settingsRepository)
                        )
                        FinanceApp(viewModel = viewModel)
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "App is locked",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Button(onClick = { showBiometricPrompt() }) {
                                    Text("Unlock")
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (requiresAuth && !isAuthenticated) {
            showBiometricPrompt()
        }
    }

    override fun onStop() {
        super.onStop()
        if (requiresAuth) {
            isAuthenticated = false
        }
    }

    private fun showBiometricPrompt() {
        val executor= ContextCompat.getMainExecutor(this)
        val biometricPrompt= BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult ) {
                    super.onAuthenticationSucceeded(result)
                    isAuthenticated = true
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Optionally close app if auth fails or let them retry
                }
            } )

        val promptInfo =BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Entry")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }


}