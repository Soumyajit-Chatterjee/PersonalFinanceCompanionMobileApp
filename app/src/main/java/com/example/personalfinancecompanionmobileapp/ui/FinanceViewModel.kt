package com.example.personalfinancecompanionmobileapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalfinancecompanionmobileapp.data.model.SavingsGoal
import com.example.personalfinancecompanionmobileapp.data.model.Transaction
import com.example.personalfinancecompanionmobileapp.data.model.TransactionType
import com.example.personalfinancecompanionmobileapp.data.repository.FinanceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.personalfinancecompanionmobileapp.data.repository.SettingsRepository

class FinanceViewModel(
    private val repository: FinanceRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val transactions: StateFlow<List<Transaction>> = repository.getTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savingsGoal: StateFlow<SavingsGoal> = repository.getSavingsGoal()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SavingsGoal(500.0) // default fallback
        )


    val totalIncome: StateFlow<Double> = transactions.map { list ->
        list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpense: StateFlow<Double> = transactions.map { list ->
        list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val currentBalance: StateFlow<Double> = transactions.map { list ->
        val inc = list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val exp = list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        inc - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.addTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }

    }

    fun deleteTransaction(id: String) {
        viewModelScope.launch {
            repository.deleteTransaction(id)
        }
    }

    suspend fun getTransactionById(id: String): Transaction? {
        return repository.getTransactionById(id)
    }


    fun updateSavingsGoal(goalAmount: Double) {
        viewModelScope.launch {
            repository.updateSavingsGoal(SavingsGoal(targetAmount = goalAmount))
        }
    }

    // Settings
    val currency: StateFlow<String> = settingsRepository.currencyFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "₹"
    )

    val theme: StateFlow<String> = settingsRepository.themeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "System"
    )
    
    val biometricEnabled: StateFlow<Boolean> = settingsRepository.biometricEnabledFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    
    val notificationEnabled: StateFlow<Boolean> = settingsRepository.notificationEnabledFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false

    )


    fun saveCurrency(currency:String) {
        viewModelScope.launch {
            settingsRepository.saveCurrency(currency)
        }
    }

    fun saveTheme(theme: String) {
        viewModelScope.launch{
            settingsRepository.saveTheme(theme)
        }

    }


    fun toggleBiometric(enabled:Boolean) {
        viewModelScope.launch {
            settingsRepository.toggleBiometric(enabled)
        }

    }

    fun saveNotificationSettings(enabled: Boolean, time:String) {
        viewModelScope.launch {
            settingsRepository.saveNotificationSettings(enabled, time)
        }

    }

}
