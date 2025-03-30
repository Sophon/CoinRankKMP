package org.example.udemykmp.features.portfolio.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userBalance")
data class UserBalanceEntity(
    @PrimaryKey val id: Int = 1,
    val cashBalance: Double,
)
