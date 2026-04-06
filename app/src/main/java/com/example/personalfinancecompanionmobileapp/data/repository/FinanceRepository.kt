package com.example.personalfinancecompanionmobileapp.data.repository

import com.example.personalfinancecompanionmobileapp.data.model.SavingsGoal
import com.example.personalfinancecompanionmobileapp.data.model.Transaction
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    fun getTransactions(): Flow<List<Transaction>>
    suspend fun getTransactionById(id: String):Transaction?
    suspend fun addTransaction(transaction:Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: String )
    
    fun getSavingsGoal(): Flow<SavingsGoal>

    suspend fun updateSavingsGoal(goal: SavingsGoal)
}
