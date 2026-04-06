package com.example.personalfinancecompanionmobileapp.data.model

import java.util.UUID

enum class TransactionType {
    INCOME, EXPENSE
}

enum class TransactionCategory(val displayName: String) {
    SALARY("Salary"),
    FREELANCE("Freelance"),
    INVESTMENT("Investment"),
    DINING("Dining & Food"),
    GROCERIES("Groceries"),
    TRANSPORT("Transport"),
    UTILITIES("Utilities"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    HEALTH("Health"),
    OTHER("Other")
}


data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val type: TransactionType,
    val category: TransactionCategory,
    val dateMillis: Long,
    val notes: String = ""
)
