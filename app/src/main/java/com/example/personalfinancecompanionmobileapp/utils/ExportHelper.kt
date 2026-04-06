package com.example.personalfinancecompanionmobileapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.personalfinancecompanionmobileapp.data.model.Transaction
import java.io.File
import java.util.concurrent.TimeUnit

object ExportHelper {
    fun exportTransactionsToCsv(context: Context, transactions: List<Transaction>): Intent? {
        return try {
            val file = File(context.cacheDir, "transactions_backup.csv")
            file.printWriter().use { out ->
                out.println("ID,Amount,Type,Category,DateMillis,Notes")
                transactions.forEach {
                    out.println("${it.id},${it.amount},${it.type.name},${it.category.name},${it.dateMillis},${it.notes.replace(",", ";")}")
                }
            }
            
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_SUBJECT, "Exported Transactions")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            Intent.createChooser(intent, "Share Data")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun setupDailyReminder(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            // Ideally calculate initial delay for 8 PM here
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_reminder",
            androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    fun cancelDailyReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("daily_reminder")
    }
}
