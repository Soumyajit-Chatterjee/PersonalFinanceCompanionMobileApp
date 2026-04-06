package com.example.personalfinancecompanionmobileapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.personalfinancecompanionmobileapp.data.repository.FinanceRepository

import com.example.personalfinancecompanionmobileapp.data.repository.SettingsRepository

class FinanceViewModelFactory(
    private val repository:FinanceRepository,
    private val settingsRepository:SettingsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
            return FinanceViewModel(repository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
