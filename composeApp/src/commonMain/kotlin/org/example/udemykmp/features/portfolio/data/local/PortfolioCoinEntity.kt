package org.example.udemykmp.features.portfolio.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolioCoinEntity")
data class PortfolioCoinEntity(
    @PrimaryKey val coinId: String,
    val name: String,
    val symbol: String,
    val iconUrl: String,
    val avgPurchasePrise: Double,
    val amountOwned: Double,
    val timestamp: Long,
)