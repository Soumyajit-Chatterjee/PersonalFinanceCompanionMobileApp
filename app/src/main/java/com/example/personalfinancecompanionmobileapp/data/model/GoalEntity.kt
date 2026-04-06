package com.example.personalfinancecompanionmobileapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate=true) val id: Int = 0,
    val targetAmount:Double,
    val name:String
)

fun GoalEntity.toDomainModel(): SavingsGoal {
    return SavingsGoal(
        targetAmount = targetAmount,
        name = name
    )

}

fun SavingsGoal.toEntity(): GoalEntity {
    return GoalEntity(
        targetAmount =targetAmount,
        name =name
    )

}
