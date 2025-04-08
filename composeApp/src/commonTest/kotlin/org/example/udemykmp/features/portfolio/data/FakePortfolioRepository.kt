package org.example.udemykmp.features.portfolio.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.EmptyResult
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.domain.coin.Coin
import org.example.udemykmp.features.portfolio.integration.PortfolioRepository
import org.example.udemykmp.features.portfolio.integration.model.PortfolioCoinModel

class FakePortfolioRepository : PortfolioRepository {
    private val _data = MutableStateFlow<Result<List<PortfolioCoinModel>, DataError.Remote>>(
        Result.Success(emptyList())
    )
    private val _cashBalance = MutableStateFlow(fakeCashBalance)
    private val _portfolioValue = MutableStateFlow(fakePortfolioValue)
    private val _coins = mutableListOf<PortfolioCoinModel>()

    override fun getPortfolioCoins(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>> {
        return _data.asStateFlow()
    }

    override suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel?, DataError.Remote> {
        return Result.Success(fakePortfolioCoin)
    }

    override suspend fun savePortfolioCoin(coin: PortfolioCoinModel): EmptyResult<DataError.Local> {
        _coins.add(coin)
        _portfolioValue.value = _coins.sumOf { it.ownedAmountInFiat }
        _data.value = Result.Success(_coins)

        return Result.Success(Unit)
    }

    override suspend fun removePortfolioCoin(coinId: String): EmptyResult<DataError.Local> {
        _coins.removeAll { it.coin.id == coinId }
        _portfolioValue.value = _coins.sumOf { it.ownedAmountInFiat }
        _data.value = Result.Success(_coins)

        return Result.Success(Unit)
    }

    override fun getTotalPortfolioCoinValue(): Flow<Result<Double, DataError.Remote>> {
        return _portfolioValue.map { Result.Success(it) }
    }

    companion object {
        val fakeCoin = Coin(
            id = "fakeId",
            name = "Fake Coin",
            symbol = "FK",
            iconUrl = "https//fake.com"
        )

        val fakePortfolioCoin = PortfolioCoinModel(
            coin = fakeCoin,
            ownedAmountInUnit = 1000.0,
            ownedAmountInFiat = 3000.0,
            performancePercent = 10.0,
            averagePurchasePrice = 10.0,
        )

        val fakeCashBalance = 10_000.0
        val fakePortfolioValue = 0.0
    }
}