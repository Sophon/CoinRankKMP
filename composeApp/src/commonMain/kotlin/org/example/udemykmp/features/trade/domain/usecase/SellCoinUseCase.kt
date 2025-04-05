package org.example.udemykmp.features.trade.domain.usecase

import kotlinx.coroutines.flow.first
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.EmptyResult
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.domain.coin.Coin
import org.example.udemykmp.core.domain.onError
import org.example.udemykmp.features.coins.integration.CoinsRepository
import org.example.udemykmp.features.portfolio.integration.BalanceRepository
import org.example.udemykmp.features.portfolio.integration.PortfolioRepository
import org.example.udemykmp.features.portfolio.integration.model.PortfolioCoinModel

//TODO: unit test
class SellCoinUseCase(
    private val coinsRepository: CoinsRepository,
    private val portfolioRepository: PortfolioRepository,
    private val userBalanceRepository: BalanceRepository,
) {
    suspend fun execute(
        coinId: String,
        amountToSellInFiat: Double
    ): EmptyResult<DataError> {
        val balance = userBalanceRepository.cashBalance().first()

        val ownedCoin = portfolioRepository.getPortfolioCoin(coinId).let { result ->
            when (result) {
                is Result.Success -> result.data
                is Result.Error -> return Result.Error(result.error)
            }
        }

        val coin = coinsRepository.getCoinDetailsOf(coinId).let { result ->
            when (result) {
                is Result.Success -> result.data
                is Result.Error -> return Result.Error(result.error)
            }
        }

        var dustAmountInFiat = 0.0
        val newCoinAmountInUnit = (ownedCoin?.ownedAmountInUnit ?: 0.0)
            .minus(amountToSellInFiat / coin.price)
            .also { if (it < 0) return Result.Error(DataError.Local.INSUFFICIENT_FUNDS) }
            .let { leftCoinAmountInUnit ->
                if (leftCoinAmountInUnit*coin.price < SELL_ALL_THRESHOLD) {
                    dustAmountInFiat = leftCoinAmountInUnit*coin.price
                    0.0
                } else leftCoinAmountInUnit
            }
        val newCoinAmountInFiat = newCoinAmountInUnit*coin.price
        val averagePurchasePrice = newCoinAmountInFiat / newCoinAmountInUnit
        val newBalance = balance + amountToSellInFiat + dustAmountInFiat

        val coinModel = ownedCoin?.copy(
            ownedAmountInUnit = newCoinAmountInUnit,
            ownedAmountInFiat = newCoinAmountInFiat,
            averagePurchasePrice = averagePurchasePrice,
        ) ?: PortfolioCoinModel(
            coin = Coin(
                id = coinId,
                name = coin.coin.name,
                symbol = coin.coin.symbol,
                iconUrl = coin.coin.iconUrl,
            ),
            performancePercent = ((coin.price - averagePurchasePrice) / averagePurchasePrice) * 100,
            averagePurchasePrice = averagePurchasePrice,
            ownedAmountInUnit = newCoinAmountInUnit,
            ownedAmountInFiat = newCoinAmountInFiat,
        )

        portfolioRepository.savePortfolioCoin(coinModel).onError { return Result.Error(it) }
        userBalanceRepository.updateCashBalance(newBalance).onError { return Result.Error(it) }

        return Result.Success(Unit)
    }
}

/**
 * If the leftover value is worth less than 1 FIAT unit, it's considered dust.
 * We don't want to keep dust, so just sell everything.
 */
private const val SELL_ALL_THRESHOLD = 1