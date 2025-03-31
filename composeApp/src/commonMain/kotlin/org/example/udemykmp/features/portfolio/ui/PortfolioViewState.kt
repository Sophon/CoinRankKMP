package org.example.udemykmp.features.portfolio.ui

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