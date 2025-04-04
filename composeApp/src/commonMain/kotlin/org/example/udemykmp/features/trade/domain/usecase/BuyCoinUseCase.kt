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
class BuyCoinUseCase(
    private val coinsRepository: CoinsRepository,
    private val portfolioRepository: PortfolioRepository,
    private val userBalanceRepository: BalanceRepository,
) {

    suspend fun execute(
        coinId: String,
        amountToBuyInFiat: Double,
    ): EmptyResult<DataError> {
        val balance = userBalanceRepository.cashBalance().first().also { userBalance ->
            if (userBalance < amountToBuyInFiat) return Result.Error(DataError.Local.INSUFFICIENT_FUNDS)
        }

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

        val newCoinAmountInUnit = ownedCoin?.ownedAmountInUnit?.plus(amountToBuyInFiat / coin.price) ?: (amountToBuyInFiat / coin.price)
        val newCoinAmountInFiat = ownedCoin?.ownedAmountInFiat?.plus(amountToBuyInFiat) ?: amountToBuyInFiat
        val averagePurchasePrice = newCoinAmountInFiat / newCoinAmountInUnit
        val newBalance = balance - amountToBuyInFiat

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