package org.example.udemykmp.features.portfolio.integration.model

data class PortfolioStatus(
    val coins: List<PortfolioCoinModel>,
    val totalOwnedCoinValue: Double,
    val cashBalance: Double,
) {
    val totalPortfolioValue: Double
        get() = totalOwnedCoinValue + cashBalance
}