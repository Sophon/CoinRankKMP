package org.example.udemykmp.features.coins.integration.usecase

import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.features.coins.integration.model.CoinModel

interface GetCoinDetailsUseCase {
    suspend fun execute(coinId: String): Result<CoinModel, DataError.Remote>
}