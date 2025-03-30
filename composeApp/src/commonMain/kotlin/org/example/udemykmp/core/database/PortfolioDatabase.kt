package org.example.udemykmp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.udemykmp.features.portfolio.data.local.PortfolioDao

@Database(entities = [PortfolioDatabase::class], version = 1)
abstract class PortfolioDatabase: RoomDatabase() {
    abstract val portfolioDao: PortfolioDao
}