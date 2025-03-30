package org.example.udemykmp.features.portfolio.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [
        PortfolioCoinEntity::class,
        UserBalanceEntity::class
    ],
    version = 1
)
@ConstructedBy(PortfolioDatabaseFactory::class)
abstract class PortfolioDatabase: RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao
    abstract fun userBalanceDao(): UserBalanceDao
}

/**
 * Room creates the expect classes itself
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object PortfolioDatabaseFactory: RoomDatabaseConstructor<PortfolioDatabase>

fun getPortfolioDatabase(
    builder: RoomDatabase.Builder<PortfolioDatabase>,
): PortfolioDatabase = builder
    .setDriver(BundledSQLiteDriver()) //Multiplatform driver
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()