package org.example.udemykmp.features.portfolio.integration

import kotlinx.coroutines.flow.Flow
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.EmptyResult

interface BalanceRepository {
    suspend fun initializeBalance()
    fun cashBalance(): Flow<Double>
    suspend fun updateCashBalance(newBalance: Double): EmptyResult<DataError.Local>
}