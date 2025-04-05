package org.example.udemykmp.features.portfolio.domain.usecase

import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.features.portfolio.integration.PortfolioRepository
import org.example.udemykmp.features.portfolio.integration.usecase.GetPortfolioCoinAmountUseCase

class GetPortfolioCoinAmountUseCaseImpl(
    private val repo: PortfolioRepository,
): GetPortfolioCoinAmountUseCase {
    override suspend fun execute(coinId: String): Double {
        return when (val result = repo.getPortfolioCoin(coinId)) {
            is Result.Success -> { result.data?.ownedAmountInFiat ?: 0.0 }
            is Result.Error -> { 0.0 }
        }
    }
}