package org.example.udemykmp.features.trade.domain.usecase

import org.example.udemykmp.features.coins.integration.CoinsRepository
import org.example.udemykmp.features.portfolio.integration.PortfolioRepository

class BuyCoinUseCase(
    private val coinsRepository: CoinsRepository,
    private val portfolioRepository: PortfolioRepository,
) {
    //TODO:
}