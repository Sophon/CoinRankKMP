package org.example.udemykmp.features.portfolio.ui

import org.example.udemykmp.core.util.formatCoinUnit
import org.example.udemykmp.core.util.formatFiat
import org.example.udemykmp.core.util.formatPercentage
import org.example.udemykmp.features.portfolio.domain.model.PortfolioCoinModel
import org.jetbrains.compose.resources.StringResource

data class PortfolioViewState(
    val portfolioValue: String = "",
    val cashBalance: String = "",
    val isBuyButtonShown: Boolean = false,
    val coins: List<UiPortfolioCoin> = emptyList(),

    val isLoading: Boolean = false,
    val error: StringResource? = null,
)

data class UiPortfolioCoin(
    val id: String,
    val name: String,
    val iconUrl: String,
    val amountInUnit: String,
    val amountInFiat: String,
    val performancePercent: String,
    val isPositivePerformance: Boolean,
)

fun PortfolioCoinModel.toUiModel(): UiPortfolioCoin {
    return UiPortfolioCoin(
        id = coin.id,
        name = coin.name,
        iconUrl = coin.iconUrl,
        amountInUnit = formatCoinUnit(ownedAmountInUnit, coin.symbol),
        amountInFiat = formatFiat(ownedAmountInFiat),
        performancePercent = formatPercentage(performancePercent),
        isPositivePerformance = performancePercent >= 0,
    )
}