package com.example.personalfinancecompanionmobileapp.ui.screens.goal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.personalfinancecompanionmobileapp.ui.theme.glassCard
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import com.example.personalfinancecompanionmobileapp.ui.FinanceViewModel

@Composable
fun GoalScreen(viewModel: FinanceViewModel) {
    val goal by viewModel.savingsGoal.collectAsState()
    var inputAmount by remember { mutableStateOf(goal.targetAmount.toString()) }
    var showSuccessPopup by remember { mutableStateOf(false) }
    
    // Update local state if the model changes from outside
    LaunchedEffect(goal.targetAmount) {
        inputAmount = goal.targetAmount.toString()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val isDark = com.example.personalfinancecompanionmobileapp.ui.theme.LocalAppDarkTheme.current
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier.fillMaxWidth().glassCard(shape = RoundedCornerShape(16.dp), darkTheme = isDark)
        ) {
            Column ( modifier = Modifier.padding(16.dp) ) {
                Text("Savings Goal", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier=Modifier.height(24.dp))
        
                Text(
                    "Setting a goal helps you stay on track. This goal represents the amount of money you want to save each month.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground

                )
        
        Spacer( modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = inputAmount,
            onValueChange = { inputAmount = it },
            label = { Text("Target Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
                Spacer(modifier=Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        val newAmount = inputAmount.toDoubleOrNull()
                        if ( newAmount != null && newAmount >= 0 ) {
                            viewModel.updateSavingsGoal(newAmount)
                            showSuccessPopup = true
                        }
                    },
                    modifier= Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Update Goal")
                }

            }
        }
    }
    
    if (showSuccessPopup) {
        LaunchedEffect(Unit) {
            delay(2000)
            showSuccessPopup = false
        }
        Dialog( onDismissRequest = { showSuccessPopup = false }) {
            val isDark = com.example.personalfinancecompanionmobileapp.ui.theme.LocalAppDarkTheme.current
            Card(
                colors= CardDefaults.cardColors( containerColor = Color.Transparent),
                elevation= CardDefaults.cardElevation(0.dp),
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .glassCard(shape = RoundedCornerShape(24.dp), darkTheme = isDark)
            ) {
                Column(
                    modifier =Modifier.padding(24.dp),
                    horizontalAlignment =Alignment.CenterHorizontally,
                    verticalArrangement =Arrangement.Center
                ) {
                    Text(
                        "🎉",
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        "Congrats on setting new milestone!",
                        style= MaterialTheme.typography.titleMedium,
                        fontWeight= FontWeight.Bold,
                        textAlign= TextAlign.Center,
                        color =MaterialTheme.colorScheme.onBackground
                    )
                }
            }

        }
    }

}
