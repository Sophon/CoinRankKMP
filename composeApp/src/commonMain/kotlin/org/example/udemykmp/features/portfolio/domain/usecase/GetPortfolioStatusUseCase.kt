package org.example.udemykmp.features.portfolio.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.features.portfolio.data.BalanceRepository
import org.example.udemykmp.features.portfolio.data.PortfolioRepository
import org.example.udemykmp.features.portfolio.domain.model.PortfolioStatus

class GetPortfolioStatusUseCase(
    private val portfolioRepository: PortfolioRepository,
    private val balanceRepository: BalanceRepository,
) {
    fun execute(): Flow<Result<PortfolioStatus, DataError.Remote>> {
        return combine(
            portfolioRepository.getPortfolioCoins(),
            portfolioRepository.getTotalPortfolioCoinValue(),
            balanceRepository.cashBalance(),
        ) { portfolioCoinsResult, totalPortfolioCoinValueResult, cashAmount ->
            when (portfolioCoinsResult) {
                is Result.Success -> {
                    val totalPortfolioCoinValue = when (totalPortfolioCoinValueResult) {
                        is Result.Success -> totalPortfolioCoinValueResult.data
                        is Result.Error -> 0.0
                    }

                    Result.Success(
                        PortfolioStatus(
                            coins = portfolioCoinsResult.data,
                            totalOwnedCoinValue = totalPortfolioCoinValue,
                            cashBalance = cashAmount,
                        )
                    )
                }
                is Result.Error -> {
                    Result.Error(DataError.Remote.UNKNOWN)
                }
            }
        }
    }
}