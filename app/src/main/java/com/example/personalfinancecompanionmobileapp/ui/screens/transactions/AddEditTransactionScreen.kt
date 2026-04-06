package com.example.personalfinancecompanionmobileapp.ui.screens.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.personalfinancecompanionmobileapp.data.model.Transaction
import com.example.personalfinancecompanionmobileapp.data.model.TransactionCategory
import com.example.personalfinancecompanionmobileapp.data.model.TransactionType
import com.example.personalfinancecompanionmobileapp.ui.FinanceViewModel
import androidx.compose.foundation.text.KeyboardOptions
import java.util.Calendar
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.ui.text.font.FontWeight
import com.example.personalfinancecompanionmobileapp.ui.components.getCategoryIcon
import com.example.personalfinancecompanionmobileapp.ui.components.getCategoryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    viewModel: FinanceViewModel,
    transactionId: String? = null,
    onBack: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var category by remember { mutableStateOf(TransactionCategory.DINING) }
    var notes by remember { mutableStateOf("") }
    var dateMillis by remember { mutableStateOf(Calendar.getInstance().timeInMillis) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(transactionId) {
        if (transactionId != null) {
            val existing = viewModel.getTransactionById(transactionId)
            if (existing != null) {
                amount = existing.amount.toString()
                type = existing.type
                category = existing.category
                notes = existing.notes
                dateMillis = existing.dateMillis
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(if (transactionId == null) "Add Transaction" else "Edit Transaction") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Type Segmented Button
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TransactionType.values().forEach { t ->
                    FilterChip(
                        selected = type == t,
                        onClick = { type = t },
                        label = { Text(t.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Category Selection (Visual Picker)
            Text("Select Category", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(TransactionCategory.values()) { cat ->
                    val isSelected = category == cat
                    val catColor = getCategoryColor(cat)
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { category = cat }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    color = if (isSelected) catColor else catColor.copy(alpha = 0.1f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(cat),
                                contentDescription = cat.displayName,
                                tint = if (isSelected) Color.White else catColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = cat.displayName,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Gray
                        )
                    }
                }
            }

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    if (amountDouble > 0) {
                        val newTxn = Transaction(
                            id = transactionId ?: java.util.UUID.randomUUID().toString(),
                            amount = amountDouble,
                            type = type,
                            category = category,
                            notes = notes,
                            dateMillis = dateMillis
                        )
                        if (transactionId == null) {
                            viewModel.addTransaction(newTxn)
                        } else {
                            viewModel.updateTransaction(newTxn)
                        }
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Save Transaction")
            }
        }
    }
}
