package com.example.personalfinancecompanionmobileapp.data.repository

import com.example.personalfinancecompanionmobileapp.data.db.GoalDao
import com.example.personalfinancecompanionmobileapp.data.db.TransactionDao
import com.example.personalfinancecompanionmobileapp.data.model.SavingsGoal
import com.example.personalfinancecompanionmobileapp.data.model.Transaction
import com.example.personalfinancecompanionmobileapp.data.model.toDomainModel
import com.example.personalfinancecompanionmobileapp.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomFinanceRepository(
    private val transactionDao: TransactionDao,
    private val goalDao: GoalDao
) : FinanceRepository {
    override fun getTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map { entities ->
            entities.map { it.toDomainModel() }
        }

    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return transactionDao.getTransactionById(id)?.toDomainModel()
    }

    override suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(id: String) {
        transactionDao.deleteTransaction(id)
    }

    override fun getSavingsGoal(): Flow<SavingsGoal> {
        return goalDao.getGoal().map { entity ->
            entity?.toDomainModel() ?: SavingsGoal(0.0)
        }

    }

    override suspend fun updateSavingsGoal(goal: SavingsGoal) {
        goalDao.clearGoals()
        goalDao.insertGoal(goal.toEntity())
    }

}
