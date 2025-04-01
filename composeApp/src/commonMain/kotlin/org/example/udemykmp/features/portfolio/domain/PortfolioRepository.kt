package org.example.udemykmp.features.portfolio.domain

import kotlinx.coroutines.flow.Flow
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.EmptyResult
import org.example.udemykmp.core.domain.Result

interface PortfolioRepository {
    suspend fun initializeBalance()
    fun getPortfolioCoins(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>>
    suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel, DataError.Remote>
    suspend fun savePortfolioCoin(coin: PortfolioCoinModel): EmptyResult<DataError.Local>
    suspend fun removePortfolioCoin(coinId: String): EmptyResult<DataError.Local>

    fun getTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>>
    fun getTotalBalance(): Flow<Result<Double, DataError.Remote>> //portfolio + cash
    fun cashBalance(): Flow<Double>
    suspend fun updateCashBalance(newBalance: Double): EmptyResult<DataError.Local>
}

//TODO: REFACTOR split the repo into PortfolioCoin repo and Portfolio repo