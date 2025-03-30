package org.example.udemykmp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.udemykmp.features.portfolio.data.local.PortfolioCoinEntity
import org.example.udemykmp.features.portfolio.data.local.PortfolioDao

/**
 * TODO: this is only used with feature/portfolio, move it there
 * same with the Database factory
 */
@Database(entities = [PortfolioCoinEntity::class], version = 1)
abstract class PortfolioDatabase: RoomDatabase() {
    abstract val portfolioDao: PortfolioDao
}