package org.example.udemykmp.features.coins.domain.usecases

import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.domain.map
import org.example.udemykmp.features.coins.data.mappers.toPriceModel
import org.example.udemykmp.features.coins.data.remote.CoinsRemoteDataSource
import org.example.udemykmp.features.coins.integration.model.CoinPriceHistory

class GetCoinPriceHistoryUseCase(
    private val dataSource: CoinsRemoteDataSource,
) {
    suspend fun execute(coinId: String): Result<List<CoinPriceHistory>, DataError.Remote> {
        return dataSource.getPriceHistory(coinId).map { dto ->
            dto.data.history.map { it.toPriceModel() }
        }
    }
}