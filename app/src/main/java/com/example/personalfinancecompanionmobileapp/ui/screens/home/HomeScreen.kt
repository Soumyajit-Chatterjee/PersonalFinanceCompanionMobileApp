package com.example.personalfinancecompanionmobileapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalfinancecompanionmobileapp.ui.FinanceViewModel
import com.example.personalfinancecompanionmobileapp.ui.components.TransactionItem
import com.example.personalfinancecompanionmobileapp.ui.theme.ExpenseRed
import com.example.personalfinancecompanionmobileapp.ui.theme.IncomeGreen
import com.example.personalfinancecompanionmobileapp.ui.theme.glassCard
import androidx.compose.foundation.isSystemInDarkTheme
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: FinanceViewModel,
    onNavigateToTransactions: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val balance by viewModel.currentBalance.collectAsState()
    val income by viewModel.totalIncome.collectAsState()
    val expenses by viewModel.totalExpense.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val goal by viewModel.savingsGoal.collectAsState()
    val currency by viewModel.currency.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Dashboard", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                IconButton(onClick = onNavigateToSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            BalanceCard(balance = balance, income = income, expenses = expenses, currency = currency)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            SavingsGoalSummary(
                saved = balance,
                target = goal.targetAmount,
                currency = currency
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent Transactions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                TextButton(onClick = onNavigateToTransactions) {
                    Text("See All")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        val recent = transactions.take(5)
        if (recent.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No transactions yet.", color = Color.Gray)
                }
            }
        } else {
            items(recent, key = { it.id }) { txn ->
                TransactionItem(transaction = txn, currency = currency, onClick = { /* Navigate to detail later */ })
            }
        }
    }
}

@Composable
fun BalanceCard(balance: Double, income: Double, expenses: Double, currency: String = "₹") {
    val isDark = com.example.personalfinancecompanionmobileapp.ui.theme.LocalAppDarkTheme.current
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(shape = RoundedCornerShape(24.dp), darkTheme = isDark)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Total Balance", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
            Text(
                text = "$currency${String.format(Locale.getDefault(), "%.2f", balance)}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Income", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
                    Text("$currency${String.format(Locale.getDefault(), "%.2f", income)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = IncomeGreen)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Expenses", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
                    Text("$currency${String.format(Locale.getDefault(), "%.2f", expenses)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = ExpenseRed)
                }
            }
        }
    }
}

@Composable
fun SavingsGoalSummary(saved: Double, target: Double, currency: String = "₹") {
    val progress = if (target > 0) (saved / target).toFloat().coerceIn(0f, 1f) else 0f
    val isDark = com.example.personalfinancecompanionmobileapp.ui.theme.LocalAppDarkTheme.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(shape = RoundedCornerShape(16.dp), darkTheme = isDark)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Monthly Savings Goal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).background(Color.Transparent, RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("$currency${String.format(Locale.getDefault(), "%.0f", saved)} saved", fontSize = 12.sp)
                Text("$currency${String.format(Locale.getDefault(), "%.0f", target)} target", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
