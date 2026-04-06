package com.example.personalfinancecompanionmobileapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.personalfinancecompanionmobileapp.data.model.Transaction
import com.example.personalfinancecompanionmobileapp.data.model.TransactionType
import com.example.personalfinancecompanionmobileapp.ui.theme.ExpenseRed
import com.example.personalfinancecompanionmobileapp.ui.theme.IncomeGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.personalfinancecompanionmobileapp.ui.theme.glassCard
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun TransactionItem(
    transaction: Transaction,
    currency: String = "₹",
    onClick: () -> Unit
) {
    val isDark = com.example.personalfinancecompanionmobileapp.ui.theme.LocalAppDarkTheme.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .glassCard(shape = CircleShape, darkTheme = isDark)
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val categoryIconColor = MaterialTheme.colorScheme.primary
            Box(
                modifier = Modifier
                    .padding(start = 8.dp) // extra padding inside pill
                    .size(48.dp)
                    .background(
                        color = categoryIconColor.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = transaction.category.displayName,
                    tint = categoryIconColor,
                    modifier = Modifier.padding(12.dp) // Add padding so it strictly fits inside without cropping
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f).padding(all = 8.dp)) {
                Text(
                    text = transaction.category.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = transaction.notes.ifEmpty { "No notes" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(end = 16.dp)) {
                Text(
                    text = "${if (transaction.type == TransactionType.EXPENSE) "-" else "+"}$currency${String.format(Locale.getDefault(), "%.2f", transaction.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.type == TransactionType.EXPENSE) ExpenseRed else IncomeGreen
                )
                val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
                Text(
                    text = sdf.format(Date(transaction.dateMillis)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}
