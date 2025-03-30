package org.example.udemykmp.features.portfolio.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Upsert
    suspend fun insert(portfolioCoinEntity: PortfolioCoinEntity)

    @Query("SELECT * FROM portfolioCoinEntity")
    fun getAllCoins(): Flow<List<PortfolioCoinEntity>>

    @Query("SELECT * FROM portfolioCoinEntity WHERE coinId = :coinId ORDER BY timestamp DESC")
    fun getCoin(coinId: String): Flow<PortfolioCoinEntity?>

    @Query("DELETE FROM portfolioCoinEntity WHERE coinId = :coinId")
    suspend fun deleteCoin(coinId: String)
}