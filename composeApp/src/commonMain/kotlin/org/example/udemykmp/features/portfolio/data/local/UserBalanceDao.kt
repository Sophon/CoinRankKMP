package org.example.udemykmp.features.portfolio.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserBalanceDao {
    @Query("SELECT cashBalance FROM userBalance")
    suspend fun getCashBalance(): Double?

    @Upsert
    suspend fun insertBalance(userBalanceEntity: UserBalanceEntity)

    @Query("UPDATE userBalance SET cashBalance = :newBalance WHERE id = 1")
    suspend fun updateBalance(newBalance: Double)
}