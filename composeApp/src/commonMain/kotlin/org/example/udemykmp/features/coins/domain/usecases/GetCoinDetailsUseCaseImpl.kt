package org.example.udemykmp.features.coins.domain.usecases

import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.domain.map
import org.example.udemykmp.features.coins.data.mappers.toCoinModel
import org.example.udemykmp.features.coins.data.remote.CoinsRemoteDataSource
import org.example.udemykmp.features.coins.integration.model.CoinModel
import org.example.udemykmp.features.coins.integration.usecase.GetCoinDetailsUseCase

class GetCoinDetailsUseCaseImpl(
    private val dataSource: CoinsRemoteDataSource,
): GetCoinDetailsUseCase {
    override suspend fun execute(coinId: String): Result<CoinModel, DataError.Remote> {
        return dataSource.getCoinDetailsOf(coinId).map { dto ->
            dto.data.coin.toCoinModel()
        }
    }
}