package com.example.personalfinancecompanionmobileapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.personalfinancecompanionmobileapp.data.model.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals LIMIT 1")
    fun getGoal(): Flow<GoalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)
    
    @Query("DELETE FROM goals")
    suspend fun clearGoals()
}
