package org.example.udemykmp.core.database

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

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