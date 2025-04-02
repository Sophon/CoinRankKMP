package org.example.udemykmp.features.coins.domain.usecases

import org.example.udemykmp.features.coins.data.mappers.toPriceModel
import org.example.udemykmp.features.coins.integration.CoinsRemoteDataSource
import org.example.udemykmp.features.coins.domain.model.CoinPriceHistory
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.domain.map

class GetCoinPriceHistoryUseCase(
    private val dataSource: CoinsRemoteDataSource,
) {
    suspend fun execute(coinId: String): Result<List<CoinPriceHistory>, DataError.Remote> {
        return dataSource.getPriceHistory(coinId).map { dto ->
            dto.data.history.map { it.toPriceModel() }
        }
    }
}