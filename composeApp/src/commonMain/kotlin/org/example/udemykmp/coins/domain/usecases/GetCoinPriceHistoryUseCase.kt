package org.example.udemykmp.coins.domain.usecases

import org.example.udemykmp.coins.data.mappers.toPriceModel
import org.example.udemykmp.coins.domain.api.CoinsRemoteDataSource
import org.example.udemykmp.coins.domain.model.CoinPriceHistory
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