package com.example.personalfinancecompanionmobileapp.ui.screens.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalfinancecompanionmobileapp.ui.FinanceViewModel
import com.example.personalfinancecompanionmobileapp.ui.components.TransactionItem
import com.example.personalfinancecompanionmobileapp.ui.components.CategoryDonutChart
import com.example.personalfinancecompanionmobileapp.data.model.TransactionType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import com.example.personalfinancecompanionmobileapp.data.model.TransactionCategory
import androidx.compose.foundation.background

@Composable
fun TransactionListScreen(
    viewModel: FinanceViewModel,
    onAddTransaction: () -> Unit,
    onEditTransaction: (String) -> Unit
) {
    val transactions by viewModel.transactions.collectAsState()
    val currency by viewModel.currency.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<TransactionCategory?>(null) }

    val filteredTransactions = remember(transactions, searchQuery, selectedCategory) {
        transactions.filter { txn ->
            val matchesSearch = txn.notes.contains(searchQuery, ignoreCase = true) || 
                                txn.amount.toString().contains(searchQuery)
            val matchesCategory = selectedCategory == null || txn.category == selectedCategory
            matchesSearch && matchesCategory
        }
    }

    // Calculate sum of expenses per category for the Donut Chart (still using unfiltered for general breakdown or filtered? Let's use filtered!)
    val expensesByCategory = remember(filteredTransactions) {
        filteredTransactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransaction,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp) // space for FAB
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Expense Breakdown",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        if (expensesByCategory.isNotEmpty()) {
                            CategoryDonutChart(
                                expensesByCategory = expensesByCategory,
                                currency = currency,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        } else {
                            Text("No expenses yet.", modifier = Modifier.padding(32.dp))
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search transactions") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                FilterChip(
                                    selected = selectedCategory == null,
                                    onClick = { selectedCategory = null },
                                    label = { Text("All") }
                                )
                            }
                            items(TransactionCategory.values()) { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat.displayName) }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                item {
                    Text(
                        "All Transactions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                if (filteredTransactions.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No transactions match.", color = Color.Gray)
                        }
                    }
                } else {
                    items(filteredTransactions, key = { it.id }) { txn ->
                        @OptIn(ExperimentalMaterial3Api::class)
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.deleteTransaction(txn.id)
                                    true
                                } else if (dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                                    onEditTransaction(txn.id)
                                    false
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                val isDeleting = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
                                val isEditing = dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd
                                val color = when {
                                    isDeleting -> Color.Red.copy(alpha = 0.8f)
                                    isEditing -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                    else -> Color.Transparent
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .background(color, androidx.compose.foundation.shape.CircleShape),
                                    contentAlignment = if (isDeleting) Alignment.CenterEnd else Alignment.CenterStart
                                ) {
                                    if (isDeleting) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            modifier = Modifier.padding(end = 24.dp),
                                            tint = Color.White
                                        )
                                    } else if (isEditing) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            modifier = Modifier.padding(start = 24.dp),
                                            tint = Color.White
                                        )
                                    }
                                }
                            },
                            content = {
                                TransactionItem(
                                    transaction = txn,
                                    currency = currency,
                                    onClick = { onEditTransaction(txn.id) }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
