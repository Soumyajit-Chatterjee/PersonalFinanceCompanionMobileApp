package com.example.personalfinancecompanionmobileapp.ui.screens.insights

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalfinancecompanionmobileapp.data.model.TransactionType
import com.example.personalfinancecompanionmobileapp.ui.FinanceViewModel
import java.util.Locale
import com.example.personalfinancecompanionmobileapp.ui.theme.glassCard
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color

@Composable
fun InsightsScreen(viewModel: FinanceViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    val currency by viewModel.currency.collectAsState()

    val spendingByCategory = remember(transactions) {
        transactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
    }

    val highestCategory = spendingByCategory.firstOrNull()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Insights", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        if (highestCategory != null) {
            val isDark = com.example.personalfinancecompanionmobileapp.ui.theme.LocalAppDarkTheme.current
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCard(shape = RoundedCornerShape(16.dp), darkTheme = isDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Top Spending Category", style = MaterialTheme.typography.labelLarge)
                    Text(
                        highestCategory.first.displayName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text("$currency${String.format(Locale.getDefault(), "%.2f", highestCategory.second)}", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        } else {
            Text("Not enough data for insights.")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Spending Breakdown", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(spendingByCategory) { (category, total) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(category.displayName, style = MaterialTheme.typography.bodyLarge)
                    Text("$currency${String.format(Locale.getDefault(), "%.2f", total)}", fontWeight = FontWeight.Medium)
                }
                Divider()
            }
        }
    }
}
