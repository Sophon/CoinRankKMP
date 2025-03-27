package org.example.udemykmp.coins.domain.usecases

import org.example.udemykmp.coins.data.mappers.toCoinModel
import org.example.udemykmp.coins.domain.api.CoinsRemoteDataSource
import org.example.udemykmp.coins.domain.model.CoinModel
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.domain.map

class GetCoinsListUseCase(
    private val dataSource: CoinsRemoteDataSource,
) {
    suspend fun execute(): Result<List<CoinModel>, DataError.Remote> {
        return dataSource.getCoinsList().map { dto ->
            dto.data.coins.map { it.toCoinModel() }
        }
    }
}