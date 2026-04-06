package com.example.personalfinancecompanionmobileapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id:String,
    val amount: Double,
    val type: String, // Stored as Enum String
    val category: String, // Stored as Enum String
    val dateMillis:Long,
    val notes: String
)

fun TransactionEntity.toDomainModel(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        type = TransactionType.valueOf( type),
        category = TransactionCategory.valueOf(category ),
        dateMillis = dateMillis,
        notes =notes
    )
}


fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount= amount,
        type= type.name,
        category= category.name,
        dateMillis = dateMillis,
        notes =notes
    )

}
