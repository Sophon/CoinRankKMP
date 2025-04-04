package org.example.udemykmp.features.portfolio.integration

import kotlinx.coroutines.flow.Flow
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.EmptyResult
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.features.portfolio.integration.model.PortfolioCoinModel

interface PortfolioRepository {
    fun getPortfolioCoins(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>>
    suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel, DataError.Remote>
    suspend fun savePortfolioCoin(coin: PortfolioCoinModel): EmptyResult<DataError.Local>
    suspend fun removePortfolioCoin(coinId: String): EmptyResult<DataError.Local>
    fun getTotalPortfolioCoinValue(): Flow<Result<Double, DataError.Remote>>
}