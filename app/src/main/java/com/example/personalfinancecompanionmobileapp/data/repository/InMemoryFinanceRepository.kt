package com.example.personalfinancecompanionmobileapp.data.repository

import com.example.personalfinancecompanionmobileapp.data.model.SavingsGoal
import com.example.personalfinancecompanionmobileapp.data.model.Transaction
import com.example.personalfinancecompanionmobileapp.data.model.TransactionCategory
import com.example.personalfinancecompanionmobileapp.data.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class InMemoryFinanceRepository : FinanceRepository {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    private val _savingsGoal = MutableStateFlow(SavingsGoal(targetAmount = 500.0))

    init {
        // Populate with some realistic dummy data
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val yesterday = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_MONTH, -3)
        val aFewDaysAgo = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_MONTH, -10)
        val older = calendar.timeInMillis

        _transactions.value = listOf(
            Transaction(amount = 2500.0, type = TransactionType.INCOME, category = TransactionCategory.SALARY, dateMillis = older, notes = "Monthly Salary"),
            Transaction(amount = 65.40, type = TransactionType.EXPENSE, category = TransactionCategory.GROCERIES, dateMillis = aFewDaysAgo, notes = "Weekend Groceries"),
            Transaction(amount = 120.0, type = TransactionType.EXPENSE, category = TransactionCategory.UTILITIES, dateMillis = yesterday, notes = "Electricity Bill"),
            Transaction(amount = 35.0, type = TransactionType.EXPENSE, category = TransactionCategory.DINING, dateMillis = now, notes = "Lunch with friend"),
            Transaction(amount = 150.0, type = TransactionType.INCOME, category = TransactionCategory.FREELANCE, dateMillis = now, notes = "Side gig")
        ).sortedByDescending { it.dateMillis }
    }

    override fun getTransactions(): Flow<List<Transaction>> = _transactions.asStateFlow()

    override suspend fun getTransactionById(id: String): Transaction? {
        return _transactions.value.find { it.id == id }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        _transactions.update { current ->
            (current + transaction).sortedByDescending { it.dateMillis }
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        _transactions.update { current ->
            current.map { if (it.id == transaction.id) transaction else it }
                .sortedByDescending { it.dateMillis }
        }
    }

    override suspend fun deleteTransaction(id: String) {
        _transactions.update { current ->
            current.filter { it.id != id }
        }
    }

    override fun getSavingsGoal(): Flow<SavingsGoal> = _savingsGoal.asStateFlow()

    override suspend fun updateSavingsGoal(goal: SavingsGoal) {
        _savingsGoal.value = goal
    }
}
